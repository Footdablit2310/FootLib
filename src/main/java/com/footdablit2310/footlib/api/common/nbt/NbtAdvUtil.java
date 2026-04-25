package com.footdablit2310.footlib.api.common.nbt;

import java.util.OptionalInt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * FootLib NBT utility.
 * put = save
 * get = load
 *
 * Status codes:
 *  1 = key present and type correct
 *  0 = key missing (fallback used)
 * -1 = key present but wrong type
 */
public final class NbtAdvUtil {

    private NbtAdvUtil() {}

    // -------------------------------------------------------------------------
    // Status helpers
    // -------------------------------------------------------------------------
    private static OptionalInt status(int code, boolean returnStatusCode) {
        return returnStatusCode ? OptionalInt.of(code) : OptionalInt.empty();
    }

    private static boolean typeMatches(CompoundTag tag, String key, int expectedType) {
        return tag.contains(key) && tag.getTagType(key) == expectedType;
    }

    // -------------------------------------------------------------------------
    // Basic types (save)
    // -------------------------------------------------------------------------
    public static OptionalInt putIfNotNull(CompoundTag tag, String key, String value, boolean returnStatusCode) {
        if (value != null) {
            tag.putString(key, value);
            return status(1, returnStatusCode);
        }
        return status(0, returnStatusCode);
    }

    public static OptionalInt putIfNotNull(CompoundTag tag, String key, Integer value, boolean returnStatusCode) {
        if (value != null) {
            tag.putInt(key, value);
            return status(1, returnStatusCode);
        }
        return status(0, returnStatusCode);
    }

    public static OptionalInt putIfNotNull(CompoundTag tag, String key, Boolean value, boolean returnStatusCode) {
        if (value != null) {
            tag.putBoolean(key, value);
            return status(1, returnStatusCode);
        }
        return status(0, returnStatusCode);
    }

    public static OptionalInt putIfNotNull(CompoundTag tag, String key, Float value, boolean returnStatusCode) {
        if (value != null) {
            tag.putFloat(key, value);
            return status(1, returnStatusCode);
        }
        return status(0, returnStatusCode);
    }

    public static OptionalInt putIfNotNull(CompoundTag tag, String key, Double value, boolean returnStatusCode) {
        if (value != null) {
            tag.putDouble(key, value);
            return status(1, returnStatusCode);
        }
        return status(0, returnStatusCode);
    }

    // -------------------------------------------------------------------------
    // Basic types (load)
    // -------------------------------------------------------------------------
    public static Tuple<String, OptionalInt> getString(CompoundTag tag, String key, String fallback, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(fallback, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_STRING)) return new Tuple<>(fallback, status(-1, returnStatusCode));
        return new Tuple<>(tag.getString(key), status(1, returnStatusCode));
    }

    public static Tuple<Integer, OptionalInt> getInt(CompoundTag tag, String key, int fallback, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(fallback, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_INT)) return new Tuple<>(fallback, status(-1, returnStatusCode));
        return new Tuple<>(tag.getInt(key), status(1, returnStatusCode));
    }

    public static Tuple<Boolean, OptionalInt> getBool(CompoundTag tag, String key, boolean fallback, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(fallback, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_BYTE)) return new Tuple<>(fallback, status(-1, returnStatusCode));
        return new Tuple<>(tag.getBoolean(key), status(1, returnStatusCode));
    }

    public static Tuple<Float, OptionalInt> getFloat(CompoundTag tag, String key, float fallback, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(fallback, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_FLOAT)) return new Tuple<>(fallback, status(-1, returnStatusCode));
        return new Tuple<>(tag.getFloat(key), status(1, returnStatusCode));
    }

    public static Tuple<Double, OptionalInt> getDouble(CompoundTag tag, String key, double fallback, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(fallback, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_DOUBLE)) return new Tuple<>(fallback, status(-1, returnStatusCode));
        return new Tuple<>(tag.getDouble(key), status(1, returnStatusCode));
    }

    // -------------------------------------------------------------------------
    // ItemStackHandler
    // -------------------------------------------------------------------------
    public static void putItemHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        tag.put(key, handler.serializeNBT(provider));
    }

    public static OptionalInt getItemHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider, boolean returnStatusCode) {
        if (!tag.contains(key)) return status(0, returnStatusCode);
        handler.deserializeNBT(provider, tag.getCompound(key));
        return status(1, returnStatusCode);
    }

    // -------------------------------------------------------------------------
    // Energy (save/load raw value only)
    // -------------------------------------------------------------------------
    public static void putEnergy(CompoundTag tag, String key, EnergyStorage energy) {
        tag.putInt(key, energy.getEnergyStored());
    }

    public static Tuple<Integer, OptionalInt> getEnergy(CompoundTag tag, String key, boolean returnStatusCode) {
        if (!tag.contains(key)) return new Tuple<>(0, status(0, returnStatusCode));
        if (!typeMatches(tag, key, Tag.TAG_INT)) return new Tuple<>(0, status(-1, returnStatusCode));
        return new Tuple<>(tag.getInt(key), status(1, returnStatusCode));
    }

    // -------------------------------------------------------------------------
    // Optional helpers
    // -------------------------------------------------------------------------
    public static Tuple<Integer, OptionalInt> addEnergy(int current, int amount, int capacity, boolean returnStatusCode) {
        int newEnergy = Math.min(capacity, current + Math.max(0, amount));
        boolean changed = newEnergy != current;
        return new Tuple<>(newEnergy, status(changed ? 1 : 0, returnStatusCode));
    }

    public static Tuple<Integer, OptionalInt> subtractEnergy(int current, int amount, boolean returnStatusCode) {
        int newEnergy = Math.max(0, current - Math.max(0, amount));
        boolean changed = newEnergy != current;
        return new Tuple<>(newEnergy, status(changed ? 1 : 0, returnStatusCode));
    }
}
