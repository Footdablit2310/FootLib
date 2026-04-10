package com.footdablit2310.footlib.api.common;

import com.google.gson.JsonObject;

public final class ItemJsonUtil {

    private ItemJsonUtil() {}

    public static JsonObject item(String itemId, int count) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", itemId);
        if (count > 1) {
            obj.addProperty("count", count);
        }
        return obj;
    }

    public static JsonObject itemTag(String tagId, int count) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tag", tagId);
        if (count > 1) {
            obj.addProperty("count", count);
        }
        return obj;
    }
}
