package com.footdablit2310.footlib.api.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JsonUtil {

    private JsonUtil() {}

    public static void putIfNotNull(JsonObject json, String key, String value) {
        if (value != null) {
            json.addProperty(key, value);
        }
    }

    public static void putIfNotNull(JsonObject json, String key, Number value) {
        if (value != null) {
            json.addProperty(key, value);
        }
    }

    public static void putIfNotNull(JsonObject json, String key, Boolean value) {
        if (value != null) {
            json.addProperty(key, value);
        }
    }

    public static String getString(JsonObject json, String key, String fallback) {
        JsonElement e = json.get(key);
        return e != null && e.isJsonPrimitive() ? e.getAsString() : fallback;
    }

    public static int getInt(JsonObject json, String key, int fallback) {
        JsonElement e = json.get(key);
        return e != null && e.isJsonPrimitive() ? e.getAsInt() : fallback;
    }

    public static boolean getBool(JsonObject json, String key, boolean fallback) {
        JsonElement e = json.get(key);
        return e != null && e.isJsonPrimitive() ? e.getAsBoolean() : fallback;
    }
}