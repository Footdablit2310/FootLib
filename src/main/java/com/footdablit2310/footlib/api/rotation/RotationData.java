package com.footdablit2310.footlib.api.rotation;

import net.minecraft.nbt.CompoundTag;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

/**
 * Double-precision rotation state with optional tick-based spherical
 * interpolation. All getters return defensive copies.
 */
public final class RotationData {
    public static final String NBT_KEY = "FootLibRotation";
    private final Quaterniond rotation = new Quaterniond();
    private final Quaterniond target = new Quaterniond();
    private int interpolationDuration;
    private int interpolationElapsed;

    public RotationData() {
    }

    public RotationData(Quaterniondc initial) {
        set(initial);
    }

    /** Current rotation, suitable for rendering. */
    public Quaterniond get() {
        return new Quaterniond(rotation);
    }

    /** Final destination of an active interpolation. */
    public Quaterniond getTarget() {
        return new Quaterniond(target);
    }

    /** Immediately replaces both current and target rotation. */
    public void set(Quaterniondc value) {
        validate(value);
        rotation.set(value).normalize();
        target.set(rotation);
        interpolationDuration = 0;
        interpolationElapsed = 0;
    }

    /** Starts a smooth shortest-path quaternion interpolation. */
    public void setTarget(Quaterniondc value, int durationTicks) {
        validate(value);
        if (durationTicks < 0) {
            throw new IllegalArgumentException("durationTicks cannot be negative");
        }
        target.set(value).normalize();
        interpolationDuration = durationTicks;
        interpolationElapsed = 0;
        if (durationTicks == 0) {
            rotation.set(target);
        }
    }

    /** Call once per server and client BlockEntity tick. */
    public boolean tick() {
        if (interpolationDuration <= 0 || interpolationElapsed >= interpolationDuration) {
            return false;
        }
        interpolationElapsed++;
        double alpha = (double) interpolationElapsed / interpolationDuration;
        rotation.slerp(target, alpha).normalize();
        if (interpolationElapsed >= interpolationDuration) {
            rotation.set(target);
            interpolationDuration = 0;
            interpolationElapsed = 0;
        }
        return true;
    }

    public boolean isInterpolating() {
        return interpolationDuration > 0;
    }

    private static void validate(Quaterniondc value) {
        if (value == null) {
            throw new IllegalArgumentException("Rotation quaternion cannot be null");
        }
        double lengthSquared = value.lengthSquared();
        if (!Double.isFinite(lengthSquared) || lengthSquared < 1.0E-24) {
            throw new IllegalArgumentException("Rotation quaternion must be finite and non-zero");
        }
    }

    public void load(CompoundTag parent) {
        if (!parent.contains(NBT_KEY, CompoundTag.TAG_COMPOUND)) {
            set(new Quaterniond());
            return;
        }

        CompoundTag tag = parent.getCompound(NBT_KEY);
        Quaterniond loadedRotation = readQuaternion(tag, "current");
        Quaterniond loadedTarget = readQuaternion(tag, "target");
        set(loadedRotation);
        int duration = tag.getInt("duration");
        int elapsed = tag.getInt("elapsed");
        if (duration > 0 && elapsed < duration) {
            target.set(loadedTarget);
            interpolationDuration = duration;
            interpolationElapsed = Math.max(0, elapsed);
        }
    }

    public void save(CompoundTag parent) {
        CompoundTag tag = new CompoundTag();
        writeQuaternion(tag, "current", rotation);
        writeQuaternion(tag, "target", target);
        tag.putInt("duration", interpolationDuration);
        tag.putInt("elapsed", interpolationElapsed);
        parent.put(NBT_KEY, tag);
    }

    private static Quaterniond readQuaternion(CompoundTag tag, String key) {
        CompoundTag value = tag.getCompound(key);
        return new Quaterniond(
                value.getDouble("x"), value.getDouble("y"),
                value.getDouble("z"), value.getDouble("w")
        ).normalize();
    }

    private static void writeQuaternion(CompoundTag tag, String key, Quaterniondc value) {
        CompoundTag quaternion = new CompoundTag();
        quaternion.putDouble("x", value.x());
        quaternion.putDouble("y", value.y());
        quaternion.putDouble("z", value.z());
        quaternion.putDouble("w", value.w());
        tag.put(key, quaternion);
    }
}
