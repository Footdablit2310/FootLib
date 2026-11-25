package com.footdablit2310.footlib.api.rotation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniond;

import java.util.Optional;

/** Public entry point for FootLib's BlockEntity-only quaternion rotation API. */
public final class RotationAPI {
    public static final double PRECISION_TOLERANCE = 1.0E-7;

    private RotationAPI() {}

    public static boolean isRotatable(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity;
    }

    /**
     * Returns the effective rotation: the resolved JSON blockstate base
     * quaternion multiplied by the BlockEntity's mutable quaternion offset.
     */
    public static Optional<Quaterniond> getRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        Quaterniond base = baseRotation(blockEntity);
        return Optional.of(base.mul(rotatable.getRotation()).normalize());
    }

    public static Optional<Quaterniond> getTargetRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        Quaterniond base = baseRotation(blockEntity);
        return Optional.of(base.mul(rotatable.footlib$getRotationData().getTarget()).normalize());
    }

    public static boolean isInterpolating(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity rotatable
                && rotatable.footlib$getRotationData().isInterpolating();
    }

    /** Sets an absolute effective rotation, accounting for the JSON base rotation. */
    public static boolean setRotation(BlockEntity blockEntity, Quaterniond rotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        validate(rotation);
        Quaterniond offset = baseRotation(blockEntity).invert().mul(new Quaterniond(rotation)).normalize();
        rotatable.setRotation(offset);
        notifyChanged(blockEntity);
        return true;
    }

    /** Applies a local-space delta: effectiveRotation * deltaRotation. */
    public static boolean rotate(BlockEntity blockEntity, Quaterniond deltaRotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        validate(deltaRotation);

        Quaterniond effective = baseRotation(blockEntity).mul(rotatable.getRotation()).normalize();
        Quaterniond targetEffective = effective.mul(new Quaterniond(deltaRotation)).normalize();
        Quaterniond targetOffset = baseRotation(blockEntity).invert().mul(targetEffective).normalize();

        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        int duration = settings != null && settings.smooth() ? settings.interpolationTicks() : 0;
        rotatable.footlib$getRotationData().setTarget(targetOffset, duration);
        notifyChanged(blockEntity);
        return true;
    }

    public static boolean approximatelyEquals(Quaterniond first, Quaterniond second) {
        boolean sameSign = Math.abs(first.x - second.x) <= PRECISION_TOLERANCE
                && Math.abs(first.y - second.y) <= PRECISION_TOLERANCE
                && Math.abs(first.z - second.z) <= PRECISION_TOLERANCE
                && Math.abs(first.w - second.w) <= PRECISION_TOLERANCE;
        boolean oppositeSign = Math.abs(first.x + second.x) <= PRECISION_TOLERANCE
                && Math.abs(first.y + second.y) <= PRECISION_TOLERANCE
                && Math.abs(first.z + second.z) <= PRECISION_TOLERANCE
                && Math.abs(first.w + second.w) <= PRECISION_TOLERANCE;
        return sameSign || oppositeSign;
    }

    private static Quaterniond baseRotation(BlockEntity blockEntity) {
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        return settings == null ? new Quaterniond() : settings.rotation();
    }

    private static boolean isServerSide(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        return level == null || !level.isClientSide();
    }

    private static void validate(Quaterniond value) {
        if (value == null || !Double.isFinite(value.lengthSquared()) || value.lengthSquared() < 1.0E-24) {
            throw new IllegalArgumentException("Rotation quaternion must be finite and non-zero");
        }
    }

    private static void notifyChanged(BlockEntity blockEntity) {
        blockEntity.setChanged();
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockPos pos = blockEntity.getBlockPos();
            level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
    }
}
