package com.footdablit2310.footlib.api.rotation;

import net.minecraft.nbt.CompoundTag;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;

/**
 * Mutable, double-precision rotation state for a BlockEntity.
 *
 * <p>Quaternions are stored in JOML's (x, y, z, w) convention. Values returned
 * by this class are defensive copies so callers cannot mutate stored state
 * without going through {@link #set(Quaterniondc)}.</p>
 */
public final class RotationData {
    public static final String NBT_KEY = "FootLibRotation";
    private final Quaterniond rotation = new Quaterniond();

    public RotationData() {
    }

    public RotationData(Quaterniondc initial) {
        set(initial);
    }

    public Quaterniond get() {
        return new Quaterniond(rotation);
    }

    public void set(Quaterniondc value) {
        if (value == null) {
            throw new IllegalArgumentException("Rotation quaternion cannot be null");
        }

        double lengthSquared = value.lengthSquared();
        if (!Double.isFinite(lengthSquared) || lengthSquared < 1.0E-24) {
            throw new IllegalArgumentException("Rotation quaternion must be finite and non-zero");
        }

        rotation.set(value).normalize();
    }

    public void load(CompoundTag parent) {
        if (!parent.contains(NBT_KEY, CompoundTag.TAG_COMPOUND)) {
            rotation.identity();
            return;
        }

        CompoundTag tag = parent.getCompound(NBT_KEY);
        set(new Quaterniond(
                tag.getDouble("x"),
                tag.getDouble("y"),
                tag.getDouble("z"),
                tag.getDouble("w")
        ));
    }

    public void save(CompoundTag parent) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", rotation.x);
        tag.putDouble("y", rotation.y);
        tag.putDouble("z", rotation.z);
        tag.putDouble("w", rotation.w);
        parent.put(NBT_KEY, tag);
    }
}
