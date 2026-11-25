# FootLib Rotation API

Target: Minecraft 1.21.1, NeoForge 21.1.265.

## Design

- Only BlockEntities implementing `RotatableBlockEntity` can rotate.
- Rotation values are JOML `Quaterniond` (x, y, z, w), normalized on input.
- `RotationAPI.PRECISION_TOLERANCE` is 1e-7 for component comparisons.
- Model rotation is visual only. Vanilla collision, selection, and outline shapes are not transformed.
- Server-side changes are authoritative. BlockEntity implementations must include rotation data in save/load and update packets.
- Smooth changes use quaternion SLERP over the configured number of BlockEntity ticks.

## Register the rotation tool

FootLib registers `footlib:rotation_tool` in the Tools & Utilities creative tab. Use the face you click to select the rotation axis (X/Y/Z). Crouching reverses the direction.

## Make a BlockEntity rotatable

Implement `RotatableBlockEntity` and keep one `RotationData` instance:

```java
public class ExampleBlockEntity extends BlockEntity implements RotatableBlockEntity {
    private final RotationData rotation = new RotationData();

    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXAMPLE.get(), pos, state);
    }

    @Override
    public RotationData footlib$getRotationData() {
        return rotation;
    }

    // In 1.21.1, call these from the BlockEntity's save/load hooks.
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveRotation(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        loadRotation(tag);
    }

    // Call on both logical sides from the BlockEntity ticker.
    public void tick() {
        if (tickRotation() && level != null) {
            setChanged();
        }
    }
}
```

The BlockEntity's update tag/packet must also include the same rotation NBT so tracking clients receive changes. In 1.21.1, use the BlockEntity update-tag and update-packet hooks for your implementation; call `saveRotation` in the tag writer and `loadRotation` in the client tag reader.

## Public API

```java
// Returns Optional.empty() for unsupported BlockEntities.
Optional<Quaterniond> current = RotationAPI.getRotation(blockEntity);

// Immediate set (server side).
RotationAPI.setRotation(blockEntity, new Quaterniond().rotationY(Math.toRadians(45)));

// Relative local-space rotation. Uses the JSON interpolation settings.
RotationAPI.rotate(blockEntity, new Quaterniond().rotationX(Math.toRadians(90)));
```

Getters return defensive copies. Do not mutate the BlockEntity's internal quaternion directly.

## JSON configuration

Place a file at:

`data/<namespace>/footlib_rotation/<config_name>.json`

Example: `data/examplemod/footlib_rotation/example_machine.json`

```json
{
  "block": "examplemod:example_machine",
  "enabled": true,
  "rotation": {
    "rotation_step_degrees": 90.0,
    "allowed_axes": ["x", "y", "z"],
    "smooth": true,
    "interpolation_ticks": 10
  }
}
```

Configs are loaded during resource reload. The block ID in the JSON is the registry ID; the JSON filename does not have to match the block ID. Invalid configs fail reload with the offending resource identified in the exception.

## Render the model

After translating the PoseStack to the block's world origin, apply:

```java
Quaterniond rotation = RotationAPI.getRotation(blockEntity).orElseGet(Quaterniond::new);
RotationRenderHelper.apply(poseStack, rotation, new Vector3d(0.5, 0.5, 0.5));
```

The pivot is block-local coordinates. The helper converts to Minecraft's float rendering quaternion only at the PoseStack boundary; stored values and rotation math remain double precision.

## Important integration notes

1. Call `tickRotation()` once per tick from each implementing BlockEntity's ticker on both logical sides. The current quaternion is advanced with SLERP.
2. Persist rotation with `saveRotation` and `loadRotation`.
3. Include rotation in the BlockEntity's update tag/packet. `sendBlockUpdated` alone does not serialize arbitrary custom fields unless the BlockEntity supplies them.
4. Call `RotationAPI.setRotation` / `RotationAPI.rotate` on the logical server only.
5. This API intentionally does not rotate collision or selection shapes.
