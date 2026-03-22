package com.footdablit2310.footlib.api.common;

import net.minecraft.nbt.CompoundTag;

public final class Serialization {

    private Serialization() {}

    public static void putIfNotNull(CompoundTag tag, String key, String value) {
        if (value != null) {
            tag.putString(key, value);
        }
    }

    public static String getOrDefault(CompoundTag tag, String key, String fallback) {
        return tag.contains(key) ? tag.getString(key) : fallback;
    }
}