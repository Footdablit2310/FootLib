package com.footdablit2310.footlib.api.common.basic;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.energy.EnergyStorage;

public final class NbtUtil {

    private NbtUtil() {}

    // -------------------------------------------------------------------------
    // Basic types (save)
    // -------------------------------------------------------------------------
    public static void putString(CompoundTag tag, String key, String value) {
        tag.putString(key, value);
    }

    public static void putInt(CompoundTag tag, String key, int value) {
        tag.putInt(key, value);
    }

    public static void putBool(CompoundTag tag, String key, boolean value) {
        tag.putBoolean(key, value);
    }

    public static void putFloat(CompoundTag tag, String key, float value) {
        tag.putFloat(key, value);
    }

    public static void putDouble(CompoundTag tag, String key, double value) {
        tag.putDouble(key, value);
    }

    // -------------------------------------------------------------------------
    // Basic types (load)
    // -------------------------------------------------------------------------
    public static String getString(CompoundTag tag, String key) {
        return tag.getString(key);
    }

    public static int getInt(CompoundTag tag, String key) {
        return tag.getInt(key);
    }

    public static boolean getBool(CompoundTag tag, String key) {
        return tag.getBoolean(key);
    }

    public static float getFloat(CompoundTag tag, String key) {
        return tag.getFloat(key);
    }

    public static double getDouble(CompoundTag tag, String key) {
        return tag.getDouble(key);
    }

    // -------------------------------------------------------------------------
    // ItemStackHandler (NeoForge 1.21.1)
    // -------------------------------------------------------------------------
    public static void putItemHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        tag.put(key, handler.serializeNBT(provider));
    }

    public static void getItemHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        if (tag.contains(key)) {
            handler.deserializeNBT(provider, tag.getCompound(key));
        }
    }

    // -------------------------------------------------------------------------
    // Energy (save/load raw value only)
    // -------------------------------------------------------------------------
    public static void putEnergy(CompoundTag tag, String key, EnergyStorage energy) {
        tag.putInt(key, energy.getEnergyStored());
    }

    public static int getEnergy(CompoundTag tag, String key) {
        return tag.getInt(key);
    }
}
