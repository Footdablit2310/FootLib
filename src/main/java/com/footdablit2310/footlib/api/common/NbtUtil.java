package com.footdablit2310.footlib.api.common;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.energy.EnergyStorage;

public final class NbtUtil {

    private NbtUtil() {}

    // -------------------------------------------------------------------------
    // Basic types
    // -------------------------------------------------------------------------
    public static void putIfNotNull(CompoundTag tag, String key, String value) {
        if (value != null) tag.putString(key, value);
    }

    public static void putIfNotNull(CompoundTag tag, String key, Integer value) {
        if (value != null) tag.putInt(key, value);
    }

    public static void putIfNotNull(CompoundTag tag, String key, Boolean value) {
        if (value != null) tag.putBoolean(key, value);
    }

    public static String getString(CompoundTag tag, String key, String fallback) {
        return tag.contains(key) ? tag.getString(key) : fallback;
    }

    public static int getInt(CompoundTag tag, String key, int fallback) {
        return tag.contains(key) ? tag.getInt(key) : fallback;
    }

    public static boolean getBool(CompoundTag tag, String key, boolean fallback) {
        return tag.contains(key) ? tag.getBoolean(key) : fallback;
    }

    // -------------------------------------------------------------------------
    // ItemStackHandler
    // -------------------------------------------------------------------------
    public static void putItemHandler(CompoundTag tag, String key, ItemStackHandler handler) {
        tag.put(key, handler.serializeNBT());
    }

    public static void getItemHandler(CompoundTag tag, String key, ItemStackHandler handler) {
        if (tag.contains(key)) {
            handler.deserializeNBT(tag.getCompound(key));
        }
    }

    // -------------------------------------------------------------------------
    // EnergyStorage
    // -------------------------------------------------------------------------
    public static void putEnergy(CompoundTag tag, String key, EnergyStorage energy) {
        tag.putInt(key, energy.getEnergyStored());
    }

    public static void getEnergy(CompoundTag tag, String key, EnergyStorage energy) {
        if (tag.contains(key)) {
            int stored = tag.getInt(key);
            energy.receiveEnergy(stored, false);
        }
    }
}
