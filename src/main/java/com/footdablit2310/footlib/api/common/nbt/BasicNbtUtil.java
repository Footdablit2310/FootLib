package com.footdablit2310.footlib.api.common.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.HashMap;
import java.util.Map;

/**
 * BasicNbtUtil
 *
 * FootLib's complete primitive NBT utility class.
 * 
 * Supports:
 * - primitives (double, float, int, long, boolean, string)
 * - optional primitives
 * - arrays
 * - lists of strings
 * - maps (String -> String)
 *
 * This class ensures FootOP machines never touch raw NBT directly.
 */
public final class BasicNbtUtil {

    private BasicNbtUtil() {}

    // ------------------------------------------------------------
    // DOUBLE
    // ------------------------------------------------------------
    public static void writeDouble(CompoundTag tag, String key, double value) {
        tag.putDouble(key, value);
    }

    public static double readDouble(CompoundTag tag, String key) {
        return tag.getDouble(key);
    }

    public static void writeOptionalDouble(CompoundTag tag, String key, Double value) {
        if (value != null) tag.putDouble(key, value);
    }

    public static Double readOptionalDouble(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getDouble(key) : null;
    }

    // ------------------------------------------------------------
    // FLOAT
    // ------------------------------------------------------------
    public static void writeFloat(CompoundTag tag, String key, float value) {
        tag.putFloat(key, value);
    }

    public static float readFloat(CompoundTag tag, String key) {
        return tag.getFloat(key);
    }

    public static void writeOptionalFloat(CompoundTag tag, String key, Float value) {
        if (value != null) tag.putFloat(key, value);
    }

    public static Float readOptionalFloat(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getFloat(key) : null;
    }

    // ------------------------------------------------------------
    // INT
    // ------------------------------------------------------------
    public static void writeInt(CompoundTag tag, String key, int value) {
        tag.putInt(key, value);
    }

    public static int readInt(CompoundTag tag, String key) {
        return tag.getInt(key);
    }

    public static void writeOptionalInt(CompoundTag tag, String key, Integer value) {
        if (value != null) tag.putInt(key, value);
    }

    public static Integer readOptionalInt(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getInt(key) : null;
    }

    // ------------------------------------------------------------
    // LONG
    // ------------------------------------------------------------
    public static void writeLong(CompoundTag tag, String key, long value) {
        tag.putLong(key, value);
    }

    public static long readLong(CompoundTag tag, String key) {
        return tag.getLong(key);
    }

    public static void writeOptionalLong(CompoundTag tag, String key, Long value) {
        if (value != null) tag.putLong(key, value);
    }

    public static Long readOptionalLong(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getLong(key) : null;
    }

    // ------------------------------------------------------------
    // BOOLEAN
    // ------------------------------------------------------------
    public static void writeBoolean(CompoundTag tag, String key, boolean value) {
        tag.putBoolean(key, value);
    }

    public static boolean readBoolean(CompoundTag tag, String key) {
        return tag.getBoolean(key);
    }

    public static void writeOptionalBoolean(CompoundTag tag, String key, Boolean value) {
        if (value != null) tag.putBoolean(key, value);
    }

    public static Boolean readOptionalBoolean(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getBoolean(key) : null;
    }

    // ------------------------------------------------------------
    // STRING
    // ------------------------------------------------------------
    public static void writeString(CompoundTag tag, String key, String value) {
        tag.putString(key, value);
    }

    public static String readString(CompoundTag tag, String key) {
        return tag.getString(key);
    }

    public static void writeOptionalString(CompoundTag tag, String key, String value) {
        if (value != null) tag.putString(key, value);
    }

    public static String readOptionalString(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getString(key) : null;
    }

    // ------------------------------------------------------------
    // STRING LIST
    // ------------------------------------------------------------
    public static void writeStringList(CompoundTag tag, String key, Iterable<String> list) {
        ListTag listTag = new ListTag();
        for (String s : list) {
            listTag.add(StringTag.valueOf(s));
        }
        tag.put(key, listTag);
    }

    public static ListTag readStringList(CompoundTag tag, String key) {
        return tag.contains(key) ? tag.getList(key, 8) : new ListTag();
    }

    // ------------------------------------------------------------
    // STRING -> STRING MAP
    // ------------------------------------------------------------
    public static void writeStringMap(CompoundTag tag, String key, Map<String, String> map) {
        CompoundTag mapTag = new CompoundTag();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mapTag.putString(entry.getKey(), entry.getValue());
        }
        tag.put(key, mapTag);
    }

    public static Map<String, String> readStringMap(CompoundTag tag, String key) {
        Map<String, String> map = new HashMap<>();
        if (!tag.contains(key)) return map;

        CompoundTag mapTag = tag.getCompound(key);
        for (String mapKey : mapTag.getAllKeys()) {
            map.put(mapKey, mapTag.getString(mapKey));
        }
        return map;
    }
}
