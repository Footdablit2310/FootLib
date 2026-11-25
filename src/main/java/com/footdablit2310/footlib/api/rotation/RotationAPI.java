package com.footdablit2310.footlib.api.rotation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniond;

import java.util.Optional;

/**
 * Public entry point for FootLib's BlockEntity-only rotation API.
 *
 * <p>All stored and calculated rotations use JOML Quaterniond. The API does
 * not rotate collision shapes; collision remains the block's normal shape.</p>
 */
public final class RotationAPI {
    /** Numerical comparison tolerance for quaternion component comparisons. */
    public static final double PRECISION_TOLERANCE = 1.0E-7;

    private RotationAPI() {
    }

    public static boolean isRotatable(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity;
    }

    public static Optional<Quaterniond> getRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) {
            return Optional.empty();
        }
        return Optional.of(rotatable.getRotation());
    }

    /** Returns the destination quaternion when a smooth rotation is in progress. */
    public static Optional<Quaterniond> getTargetRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) {
            return Optional.empty();
        }
        return Optional.of(rotatable.footlib$getRotationData().getTarget());
    }

    /** Returns whether the BlockEntity is currently interpolating. */
    public static boolean isInterpolating(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity rotatable
                && rotatable.footlib$getRotationData().isInterpolating();
    }

    /**
     * Sets the BlockEntity rotation. The caller should invoke this on the
     * logical server; a block update is sent so tracking clients re-render.
     */
    public static boolean setRotation(BlockEntity blockEntity, Quaterniond rotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) {
            return false;
        }

        Level level = blockEntity.getLevel();
        if (level != null && level.isClientSide()) {
            return false;
        }

        rotatable.setRotation(rotation);
        blockEntity.setChanged();
        if (level != null) {
            BlockPos pos = blockEntity.getBlockPos();
            level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
        return true;
    }

    /**
     * Applies a local-space delta: currentRotation * deltaRotation.
     */
    public static boolean rotate(BlockEntity blockEntity, Quaterniond deltaRotation) {
        Optional<Quaterniond> current = getRotation(blockEntity);
        if (current.isEmpty()) {
            return false;
        }

        Quaterniond result = current.get().mul(new Quaterniond(deltaRotation)).normalize();
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) {
            return false;
        }
        Level level = blockEntity.getLevel();
        if (level != null && level.isClientSide()) {
            return false;
        }

        net.minecraft.resources.ResourceLocation blockId =
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock());
        RotationBlockConfig config = RotationConfigManager.get(blockId);
        int duration = config != null && config.smooth() ? config.interpolationTicks() : 0;
        rotatable.footlib$getRotationData().setTarget(result, duration);
        blockEntity.setChanged();
        if (level != null) {
            BlockPos pos = blockEntity.getBlockPos();
            level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
        return true;
    }

    public static boolean approximatelyEquals(Quaterniond first, Quaterniond second) {
        return Math.abs(first.x - second.x) <= PRECISION_TOLERANCE
                && Math.abs(first.y - second.y) <= PRECISION_TOLERANCE
                && Math.abs(first.z - second.z) <= PRECISION_TOLERANCE
                && Math.abs(first.w - second.w) <= PRECISION_TOLERANCE;
    }
}
