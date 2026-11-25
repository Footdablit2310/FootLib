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
        if (!(blockEntity instanceof RotatableBlockEntity)) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        return settings != null && settings.enabled();
    }

    /** Returns the effective quaternion; an untouched BlockEntity uses its blockstate default. */
    public static Optional<Quaterniond> getRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return Optional.empty();
        RotationData data = rotatable.footlib$getRotationData();
        return Optional.of(data.hasCustomRotation() ? data.get() : settings.rotation());
    }

    public static Optional<Quaterniond> getTargetRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return Optional.empty();
        RotationData data = rotatable.footlib$getRotationData();
        return Optional.of(data.hasCustomRotation() ? data.getTarget() : settings.rotation());
    }

    public static boolean isInterpolating(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity rotatable
                && rotatable.footlib$getRotationData().isInterpolating();
    }

    /**
     * Validates the raw editor quaternion, then component-adds it to the
     * configured default and normalizes only the combined effective result.
     */
    public static boolean applyInput(BlockEntity blockEntity, Quaterniond input, double toolPrecisionDegrees) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return false;
        validateInput(input);
        if (!Double.isFinite(toolPrecisionDegrees) || toolPrecisionDegrees <= 0.0 || toolPrecisionDegrees >= 360.0)
            throw new IllegalArgumentException("Tool precision must be finite and in (0, 360)");

        RotationData data = rotatable.footlib$getRotationData();
        if (!allowsInput(settings, input, data.getLastInput())) return false;
        double effectiveStep = Math.max(settings.rotationStepDegrees(), toolPrecisionDegrees);
        if (!isMultiple(representedAngleDegrees(input), effectiveStep)) return false;

        Quaterniond combined = addAndNormalize(settings.rotation(), input);
        int duration = settings.smooth() ? settings.interpolationTicks() : 0;
        if (!data.hasCustomRotation()) data.set(settings.rotation());
        data.setLastInput(input);
        data.setTarget(combined, duration);
        notifyChanged(blockEntity);
        return true;
    }

    private static boolean allowsInput(RotationSettings settings, Quaterniond input, Quaterniond previous) {
        return (settings.allowedAxes().contains("x") || Math.abs(input.x - previous.x) <= PRECISION_TOLERANCE)
                && (settings.allowedAxes().contains("y") || Math.abs(input.y - previous.y) <= PRECISION_TOLERANCE)
                && (settings.allowedAxes().contains("z") || Math.abs(input.z - previous.z) <= PRECISION_TOLERANCE)
                && (settings.allowedAxes().contains("w") || Math.abs(input.w - previous.w) <= PRECISION_TOLERANCE);
    }

    /** Component-wise addition followed by normalization. */
    public static Quaterniond addAndNormalize(Quaterniond first, Quaterniond second) {
        Quaterniond result = new Quaterniond(first.x + second.x, first.y + second.y,
                first.z + second.z, first.w + second.w);
        validateInput(result);
        return result.normalize();
    }

    public static double representedAngleDegrees(Quaterniond quaternion) {
        validateInput(quaternion);
        double vectorLength = Math.sqrt(quaternion.x * quaternion.x
                + quaternion.y * quaternion.y + quaternion.z * quaternion.z);
        return Math.toDegrees(2.0 * Math.atan2(vectorLength, Math.abs(quaternion.w)));
    }

    private static boolean isMultiple(double angle, double step) {
        double quotient = angle / step;
        double nearest = Math.rint(quotient);
        double tolerance = Math.max(1.0E-9, Math.abs(quotient) * 1.0E-10);
        return Math.abs(quotient - nearest) <= tolerance;
    }

    /** Sets an absolute effective rotation. */
    public static boolean setRotation(BlockEntity blockEntity, Quaterniond rotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return false;
        validateInput(rotation);
        rotatable.footlib$getRotationData().set(rotation);
        notifyChanged(blockEntity);
        return true;
    }

    /** Applies a local-space delta using quaternion multiplication. */
    public static boolean rotate(BlockEntity blockEntity, Quaterniond deltaRotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return false;
        validateInput(deltaRotation);
        Quaterniond effective = getRotation(blockEntity).orElseThrow();
        Quaterniond target = effective.mul(new Quaterniond(deltaRotation)).normalize();
        RotationData data = rotatable.footlib$getRotationData();
        if (!data.hasCustomRotation()) data.set(effective);
        data.setTarget(target, settings.smooth() ? settings.interpolationTicks() : 0);
        notifyChanged(blockEntity);
        return true;
    }

    public static boolean approximatelyEquals(Quaterniond first, Quaterniond second) {
        boolean same = Math.abs(first.x - second.x) <= PRECISION_TOLERANCE
                && Math.abs(first.y - second.y) <= PRECISION_TOLERANCE
                && Math.abs(first.z - second.z) <= PRECISION_TOLERANCE
                && Math.abs(first.w - second.w) <= PRECISION_TOLERANCE;
        boolean opposite = Math.abs(first.x + second.x) <= PRECISION_TOLERANCE
                && Math.abs(first.y + second.y) <= PRECISION_TOLERANCE
                && Math.abs(first.z + second.z) <= PRECISION_TOLERANCE
                && Math.abs(first.w + second.w) <= PRECISION_TOLERANCE;
        return same || opposite;
    }

    private static boolean isServerSide(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        return level == null || !level.isClientSide();
    }

    private static void validateInput(Quaterniond value) {
        if (value == null || !Double.isFinite(value.x) || !Double.isFinite(value.y)
                || !Double.isFinite(value.z) || !Double.isFinite(value.w)
                || !Double.isFinite(value.lengthSquared()) || value.lengthSquared() < 1.0E-24)
            throw new IllegalArgumentException("Rotation quaternion must be finite and non-zero");
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
