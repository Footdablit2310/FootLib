package com.footdablit2310.footlib.api.rotation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

/** Double-precision effective rotation with optional tick-based spherical interpolation. */
public final class RotationData {
    public static final String NBT_KEY = "FootLibRotation";
    private final Quaterniond rotation = new Quaterniond();
    private final Quaterniond target = new Quaterniond();
    private final Quaterniond start = new Quaterniond();
    private final Quaterniond lastInput = new Quaterniond();
    private boolean hasCustomRotation;
    private int interpolationDuration;
    private int interpolationElapsed;

    public RotationData() {}
    public RotationData(Quaterniondc initial) { set(initial); }

    public Quaterniond get() { return new Quaterniond(rotation); }
    public Quaterniond getTarget() { return new Quaterniond(target); }
    public Quaterniond getLastInput() { return new Quaterniond(lastInput); }
    public boolean hasCustomRotation() { return hasCustomRotation; }

    /** Immediately replaces the effective rotation. */
    public void set(Quaterniondc value) {
        validate(value);
        rotation.set(value).normalize();
        target.set(rotation);
        start.set(rotation);
        hasCustomRotation = true;
        interpolationDuration = 0;
        interpolationElapsed = 0;
    }

    /** Starts interpolation toward an already-combined effective quaternion. */
    public void setTarget(Quaterniondc value, int durationTicks) {
        validate(value);
        if (durationTicks < 0) throw new IllegalArgumentException("durationTicks cannot be negative");
        start.set(rotation);
        target.set(value).normalize();
        hasCustomRotation = true;
        interpolationDuration = durationTicks;
        interpolationElapsed = 0;
        if (durationTicks == 0) rotation.set(target);
    }

    /** Remembers the raw editor input separately; it is not normalized or used for interpolation. */
    public void setLastInput(Quaterniondc value) {
        validate(value);
        lastInput.set(value);
    }

    /** Call once per logical-side BlockEntity tick. */
    public boolean tick() {
        if (interpolationDuration <= 0 || interpolationElapsed >= interpolationDuration) return false;
        interpolationElapsed++;
        double alpha = (double) interpolationElapsed / interpolationDuration;
        rotation.set(start).slerp(target, alpha).normalize();
        if (interpolationElapsed >= interpolationDuration) {
            rotation.set(target);
            interpolationDuration = 0;
            interpolationElapsed = 0;
        }
        return true;
    }

    public boolean isInterpolating() { return interpolationDuration > 0; }

    public void load(CompoundTag parent) {
        reset();
        if (!parent.contains(NBT_KEY, Tag.TAG_COMPOUND)) return;
        try {
            CompoundTag tag = parent.getCompound(NBT_KEY);
            Quaterniond loadedRotation = readQuaternion(tag, "current");
            Quaterniond loadedTarget = tag.contains("target", Tag.TAG_COMPOUND)
                    ? readQuaternion(tag, "target") : new Quaterniond(loadedRotation);
            Quaterniond loadedStart = tag.contains("start", Tag.TAG_COMPOUND)
                    ? readQuaternion(tag, "start") : new Quaterniond(loadedRotation);
            Quaterniond loadedInput = tag.contains("last_input", Tag.TAG_COMPOUND)
                    ? readQuaternionRaw(tag, "last_input") : new Quaterniond();
            int duration = tag.getInt("duration");
            int elapsed = tag.getInt("elapsed");
            if (duration < 0 || duration > 1200 || elapsed < 0 || elapsed > duration)
                throw new IllegalArgumentException("Invalid rotation interpolation state");

            rotation.set(loadedRotation);
            target.set(loadedTarget);
            start.set(loadedStart);
            lastInput.set(loadedInput);
            hasCustomRotation = !tag.contains("has_custom", Tag.TAG_BYTE) || tag.getBoolean("has_custom");
            if (duration > 0 && elapsed < duration) {
                interpolationDuration = duration;
                interpolationElapsed = elapsed;
            } else {
                rotation.set(target);
            }
        } catch (RuntimeException malformed) {
            reset();
        }
    }

    public void save(CompoundTag parent) {
        CompoundTag tag = new CompoundTag();
        writeQuaternion(tag, "current", rotation);
        writeQuaternion(tag, "target", target);
        writeQuaternion(tag, "start", start);
        writeQuaternion(tag, "last_input", lastInput);
        tag.putBoolean("has_custom", hasCustomRotation);
        tag.putInt("duration", interpolationDuration);
        tag.putInt("elapsed", interpolationElapsed);
        parent.put(NBT_KEY, tag);
    }

    private void reset() {
        rotation.identity();
        target.identity();
        start.identity();
        lastInput.identity();
        hasCustomRotation = false;
        interpolationDuration = 0;
        interpolationElapsed = 0;
    }

    private static Quaterniond readQuaternion(CompoundTag parent, String key) {
        Quaterniond value = readQuaternionRaw(parent, key);
        validate(value);
        return value.normalize();
    }

    private static Quaterniond readQuaternionRaw(CompoundTag parent, String key) {
        if (!parent.contains(key, Tag.TAG_COMPOUND)) throw new IllegalArgumentException("Missing quaternion " + key);
        CompoundTag value = parent.getCompound(key);
        Quaterniond q = new Quaterniond(value.getDouble("x"), value.getDouble("y"),
                value.getDouble("z"), value.getDouble("w"));
        validate(q);
        return q;
    }

    private static void writeQuaternion(CompoundTag tag, String key, Quaterniondc value) {
        CompoundTag q = new CompoundTag();
        q.putDouble("x", value.x());
        q.putDouble("y", value.y());
        q.putDouble("z", value.z());
        q.putDouble("w", value.w());
        tag.put(key, q);
    }

    private static void validate(Quaterniondc value) {
        if (value == null || !Double.isFinite(value.x()) || !Double.isFinite(value.y())
                || !Double.isFinite(value.z()) || !Double.isFinite(value.w())
                || !Double.isFinite(value.lengthSquared()) || value.lengthSquared() < 1.0E-24)
            throw new IllegalArgumentException("Quaternion must be finite and non-zero");
    }
}
