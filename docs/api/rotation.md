# FootLib Rotation API

Target: Minecraft 1.21.1, NeoForge 21.1.265.

## Design

- Only BlockEntities implementing `RotatableBlockEntity` can rotate.
- Stored rotation offsets and all quaternion calculations use JOML `Quaterniond` (x, y, z, w).
- `RotationAPI.PRECISION_TOLERANCE` is 1e-7 for component comparisons.
- JSON provides a base quaternion and pivot for each blockstate; the BlockEntity stores a mutable quaternion offset.
- Model rotation is visual only. Vanilla collision, selection, and outline shapes are not transformed.
- Smooth changes use quaternion SLERP over the resolved number of BlockEntity ticks.

## JSON configuration

Place one file per block at:

`data/<namespace>/footlib_rotation/<block_path>.json`

The resource path identifies the block. For example, `data/examplemod/footlib_rotation/example_machine.json` configures `examplemod:example_machine`.

The root format is:

```json
{
  "default": {
    "enabled": true,
    "rotation": [0.0, 0.0, 0.0, 1.0],
    "pivot": [0.5, 0.5, 0.5],
    "smooth": true,
    "interpolation_ticks": 10,
    "rotation_step_degrees": 90.0,
    "allowed_axes": ["x", "y", "z"]
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
    }
  }
}
```

### Supported settings

| JSON property | Type | Default |
|---|---|---|
| `enabled` | boolean | `true` |
| `rotation` | 4-number array `[x,y,z,w]` | identity quaternion |
| `pivot` | 3-number array `[x,y,z]` | `[0.5,0.5,0.5]` |
| `smooth` | boolean | `true` |
| `interpolation_ticks` | integer, 0–1200 | `10` |
| `rotation_step_degrees` | number, greater than 0 and at most 360 | `90` |
| `allowed_axes` | non-empty array containing x, y, z | all axes |

All fields are optional inside a blockstate override. Omitted fields inherit the resolved default settings.

### Blockstate matching

- Keys use comma-separated property/value pairs: `facing=north,half=upper`.
- Property names and values are case-insensitive.
- An entry matches when every property in its key matches the current BlockState.
- The matching entry with the greatest number of properties wins.
- If no entry matches, `default` is used.
- Use one unambiguous most-specific matching entry for each possible state.

The JSON loader validates quaternion length, finite numbers, pivot coordinates, axes, and interpolation bounds. Quaternions are normalized when parsed.

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
// Empty when the BlockEntity does not implement RotatableBlockEntity.
Optional<Quaterniond> current = RotationAPI.getRotation(blockEntity);

// Set an absolute effective quaternion (server side).
RotationAPI.setRotation(blockEntity, new Quaterniond().rotationY(Math.toRadians(45)));

// Apply a local-space delta, using blockstate-specific interpolation settings.
RotationAPI.rotate(blockEntity, new Quaterniond().rotationX(Math.toRadians(90)));

// Resolve settings for a particular state.
RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
```

The effective rotation is the JSON blockstate base quaternion multiplied by the BlockEntity's stored quaternion offset. Getters return defensive copies.

## Render the model

After translating the PoseStack to the block's world origin:

```java
RotationRenderHelper.apply(poseStack, blockEntity);
```

This resolves the effective quaternion and pivot from the current BlockState. The stored rotation and calculations remain double precision; conversion to Minecraft's float rendering quaternion happens only at the PoseStack boundary.

## Rotation tool

FootLib registers `footlib:rotation_tool` in the Tools & Utilities creative tab. The clicked face selects the rotation axis, crouching reverses the direction, and the resolved blockstate settings control whether that axis is allowed and the rotation step size.

## Collision behaviour

The API rotates the rendered model only. Collision, selection, and outline shapes remain the block's normal vanilla shapes.
