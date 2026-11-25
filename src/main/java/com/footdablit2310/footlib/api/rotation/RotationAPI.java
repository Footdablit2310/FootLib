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

    /** Returns the configured default quaternion plus the stored user offset. */
    public static Optional<Quaterniond> getRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        Quaterniond base = baseRotation(blockEntity);
        return Optional.of(addAndNormalize(base, rotatable.getRotation()));
    }

    public static Optional<Quaterniond> getTargetRotation(BlockEntity blockEntity) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return Optional.empty();
        return Optional.of(addAndNormalize(baseRotation(blockEntity),
                rotatable.footlib$getRotationData().getTarget()));
    }

    public static boolean isInterpolating(BlockEntity blockEntity) {
        return blockEntity instanceof RotatableBlockEntity rotatable
                && rotatable.footlib$getRotationData().isInterpolating();
    }

    /**
     * Validates the user-entered quaternion against the stricter of the block
     * step and tool precision, adds it component-wise to the JSON default,
     * normalizes the result, and stores the resulting offset.
     */
    public static boolean applyInput(BlockEntity blockEntity, Quaterniond input, double toolPrecisionDegrees) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return false;
        validateInput(input);

        if (!Double.isFinite(toolPrecisionDegrees) || toolPrecisionDegrees <= 0.0 || toolPrecisionDegrees >= 360.0) {
            throw new IllegalArgumentException("Tool precision must be finite and in (0, 360)");
        }

        double effectiveStep = Math.max(settings.rotationStepDegrees(), toolPrecisionDegrees);
        double angle = representedAngleDegrees(input);
        if (!isMultiple(angle, effectiveStep)) return false;

        Quaterniond combined = addAndNormalize(settings.rotation(), input);
        Quaterniond offset = settings.rotation().invert().mul(combined).normalize();
        int duration = settings.smooth() ? settings.interpolationTicks() : 0;
        rotatable.footlib$getRotationData().setTarget(offset, duration);
        notifyChanged(blockEntity);
        return true;
    }

    /** Component-wise quaternion addition followed by normalization. */
    public static Quaterniond addAndNormalize(Quaterniond first, Quaterniond second) {
        Quaterniond result = new Quaterniond(
                first.x + second.x, first.y + second.y,
                first.z + second.z, first.w + second.w
        );
        validateInput(result);
        return result.normalize();
    }

    /** Returns the represented shortest rotation angle in [0, 180] degrees. */
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

    /** Sets an absolute effective rotation, accounting for the JSON base rotation. */
    public static boolean setRotation(BlockEntity blockEntity, Quaterniond rotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        validateInput(rotation);
        Quaterniond offset = baseRotation(blockEntity).invert().mul(new Quaterniond(rotation)).normalize();
        rotatable.setRotation(offset);
        notifyChanged(blockEntity);
        return true;
    }

    /** Applies a local-space delta using quaternion multiplication. */
    public static boolean rotate(BlockEntity blockEntity, Quaterniond deltaRotation) {
        if (!(blockEntity instanceof RotatableBlockEntity rotatable) || !isServerSide(blockEntity)) return false;
        validateInput(deltaRotation);

        Quaterniond effective = addAndNormalize(baseRotation(blockEntity), rotatable.getRotation());
        Quaterniond targetEffective = effective.mul(new Quaterniond(deltaRotation)).normalize();
        Quaterniond targetOffset = baseRotation(blockEntity).invert()
                .mul(targetEffective).normalize();

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

    private static void validateInput(Quaterniond value) {
        if (value == null || !Double.isFinite(value.x) || !Double.isFinite(value.y)
                || !Double.isFinite(value.z) || !Double.isFinite(value.w)
                || !Double.isFinite(value.lengthSquared()) || value.lengthSquared() < 1.0E-24) {
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
