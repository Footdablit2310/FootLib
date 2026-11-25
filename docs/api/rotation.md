# FootLib Quaternion Rotation API

Target: Minecraft 1.21.1, NeoForge 21.1.265.

## Design

- Only BlockEntities implementing `RotatableBlockEntity` can use the rotation API.
- Quaternions use JOML `Quaterniond` in `[x, y, z, w]` order.
- JSON provides a default quaternion and pivot for each blockstate. The BlockEntity stores the effective rotation and the last raw editor input.
- The editor is opened by using a FootLib rotation tool on an enabled rotatable BlockEntity.
- The editor submits all four quaternion components. The server independently validates the input, the open menu, the exact tool, the allowed components, and the precision step.
- Model rotation is visual only. Vanilla collision, selection, and outline shapes are not transformed.
- Smooth changes use quaternion SLERP over the configured number of BlockEntity ticks.

## JSON configuration

Place one file per block at:

`data/<namespace>/footlib_rotation/<block_path>.json`

For example, `data/examplemod/footlib_rotation/example_machine.json` configures `examplemod:example_machine`.

All seven fields are required inside `default`:

```json
{
  "default": {
    "enabled": true,
    "rotation": [0.0, 0.0, 0.0, 1.0],
    "pivot": [0.5, 0.5, 0.5],
    "smooth": true,
    "interpolation_ticks": 10,
    "rotation_step_degrees": 2.5,
    "allowed_axes": ["x", "y", "z", "w"]
  },
  "blockstates": {
    "facing=north": {
      "rotation": [0.0, 0.0, 0.0, 1.0]
    },
    "facing=south": {
      "rotation": [0.0, 1.0, 0.0, 0.0]
    },
    "facing=east": {
      "rotation": [0.0, 0.7071067811865476, 0.0, 0.7071067811865476]
    },
    "facing=west": {
      "rotation": [0.0, -0.7071067811865476, 0.0, 0.7071067811865476]
    },
    "facing=north,waterlogged=true": {
      "smooth": false
    },
    "powered=*": {
      "enabled": true
    },
    "facing=!north": {
      "interpolation_ticks": 5
    }
  }
}
```

| JSON property | Type and constraints |
|---|---|
| `enabled` | Boolean |
| `rotation` | Four finite numbers `[x,y,z,w]`; non-zero quaternion |
| `pivot` | Three finite numbers `[x,y,z]`, in block-local units |
| `smooth` | Boolean |
| `interpolation_ticks` | Integer from 0 through 1200 |
| `rotation_step_degrees` | Finite number strictly greater than 0 and strictly less than 360 |
| `allowed_axes` | Non-empty array containing any of `x`, `y`, `z`, `w` |

All fields must be present in `default`. Any subset may be present in a blockstate override; omitted values inherit from the previously resolved settings.

### Blockstate matching

- Keys use comma-separated property/value pairs, such as `facing=north,half=upper`.
- Property names and values are case-insensitive.
- Exact values match only that value.
- `*` matches any value.
- `!value` matches any value except the named value.
- Property names and values are validated against the block's actual state definition. A selector that matches no possible state is rejected.
- Matching selectors are applied from least to most specific. More matched properties take precedence; for equal property counts, more exact matches take precedence over exclusions, and exclusions take precedence over wildcards.
- If two selectors matching the same state have equal specificity, the configuration is rejected as ambiguous.

## Quaternion editor and precision

Right-click a configured rotatable BlockEntity with a rotation tool to open the X/Y/Z/W editor. The tool used to open the editor must remain in the same hand until submission; changing it invalidates the menu.

The server uses:

`effectiveStep = max(rotation_step_degrees, tool_precision_degrees)`

A larger step is less precise and therefore stricter. The represented angle of the raw submitted quaternion must be an integer multiple of this effective step. The input quaternion is validated as finite and non-zero, but is not normalized before its angle is checked.

For an input quaternion `q=(x,y,z,w)`, the represented angle is:

`2 * atan2(sqrt(x*x + y*y + z*z), abs(w))`

The check is scale-invariant and treats `q` and `-q` as the same rotation. After validation, the input is added component-wise to the configured default quaternion and the combined quaternion is normalized. The configured default is not itself checked against the tool's precision step.

`allowed_axes` controls which editor components may differ from the identity input: a disallowed X/Y/Z component must be zero, and a disallowed W component must be one. The server enforces this regardless of client UI state.

## Make a BlockEntity rotatable

Implement `RotatableBlockEntity` and keep one `RotationData` instance:

```java
public class ExampleBlockEntity extends BlockEntity implements RotatableBlockEntity {
    private final RotationData rotation = new RotationData();

    @Override
    public RotationData footlib$getRotationData() {
        return rotation;
    }

    // Include these in the BlockEntity's NBT save/load hooks.
    saveRotation(tag);
    loadRotation(tag);

    // Call from the BlockEntity ticker on both logical sides.
    tickRotation();
}
```

The BlockEntity's update tag/packet must also include the rotation NBT so tracking clients receive changes. `sendBlockUpdated` does not serialize arbitrary custom fields unless the BlockEntity supplies them in its update tag/packet.

## Public API

```java
// Empty when the BlockEntity is not rotatable or its resolved settings are disabled.
Optional<Quaterniond> current = RotationAPI.getRotation(blockEntity);

// Set an absolute effective quaternion (server side).
RotationAPI.setRotation(blockEntity, new Quaterniond().rotationY(Math.toRadians(45)));

// Apply a local-space delta, using blockstate-specific interpolation settings.
RotationAPI.rotate(blockEntity, new Quaterniond().rotationX(Math.toRadians(90)));

// Resolve settings for a particular state.
RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
```

An untouched BlockEntity uses its resolved JSON default quaternion. After an editor submission, the effective target is the normalized component-wise sum of that default and the submitted quaternion. Programmatic `setRotation` and `rotate` operate on effective rotations.

## Render the model

After translating the PoseStack to the block's world origin:

```java
RotationRenderHelper.apply(poseStack, blockEntity);
```

This resolves the effective quaternion and pivot from the current BlockState. The stored rotation and calculations remain double precision; conversion to Minecraft's float rendering quaternion happens only at the PoseStack boundary.

## Rotation tools

FootLib registers seven tools in the Tools & Utilities creative tab:

| Tool | Precision | Durability |
|---|---:|---:|
| Copper | 22.5° | 1,024 |
| Iron | 11.25° | 2,048 |
| Gold | 5° | 512 |
| Amethyst | 2.5° | 4,096 |
| Diamond | 1° | 8,192 |
| Netherite | 0.1° | 16,384 |
| Ultra Netherite | 0.0000001° | 32,768 |

A successful server-side rotation submission consumes one durability. Invalid submissions do not consume durability.

## Collision behaviour

The API rotates the rendered model only. Collision, selection, and outline shapes remain the block's normal vanilla shapes.
