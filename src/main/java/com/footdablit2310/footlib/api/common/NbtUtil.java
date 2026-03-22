package com.footdablit2310.footlib.api.common;

import net.minecraft.nbt.CompoundTag;

public final class NbtUtil {

    private NbtUtil() {}

    public static void putIfNotNull(CompoundTag tag, String key, String value) {
        if (value != null) {
            tag.putString(key, value);
        }
    }

    public static void putIfNotNull(CompoundTag tag, String key, Integer value) {
        if (value != null) {
            tag.putInt(key, value);
        }
    }

    public static void putIfNotNull(CompoundTag tag, String key, Boolean value) {
        if (value != null) {
            tag.putBoolean(key, value);
        }
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
}