package com.footdablit2310.footlib.api.rotation;

import net.minecraft.nbt.CompoundTag;
import org.joml.Quaterniond;

/**
 * Implement this interface on BlockEntities that expose a rotatable model.
 * Ordinary blocks without a BlockEntity are deliberately not supported.
 *
 * <p>The implementation owns one {@link RotationData} instance and must
 * delegate its BlockEntity save/load hooks to {@link #saveRotation(CompoundTag)}
 * and {@link #loadRotation(CompoundTag)}.</p>
 */
public interface RotatableBlockEntity {
    RotationData footlib$getRotationData();

    default Quaterniond getRotation() {
        return footlib$getRotationData().get();
    }

    default void setRotation(Quaterniond rotation) {
        footlib$getRotationData().set(rotation);
    }

    default void saveRotation(CompoundTag tag) {
        footlib$getRotationData().save(tag);
    }

    default void loadRotation(CompoundTag tag) {
        footlib$getRotationData().load(tag);
    }

    /** Call from the implementing BlockEntity's tick method. */
    default boolean tickRotation() {
        return footlib$getRotationData().tick();
    }
}
