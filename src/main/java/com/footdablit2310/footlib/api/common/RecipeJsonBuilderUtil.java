package com.footdablit2310.footlib.api.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RecipeJsonBuilderUtil {

    protected final JsonObject root = new JsonObject();

    public RecipeJsonBuilderUtil type(String type) {
        JsonUtil.putIfNotNull(root, "type", type);
        return this;
    }

    public RecipeJsonBuilderUtil input(String item, int count) {
        JsonObject in = new JsonObject();
        JsonUtil.putIfNotNull(in, "item", item);
        JsonUtil.putIfNotNull(in, "count", count);
        root.add("input", in);
        return this;
    }

    public RecipeJsonBuilderUtil outputs(JsonArray arr) {
        root.add("outputs", arr);
        return this;
    }

    public JsonObject build() {
        return root;
    }

    // Helpers
    public static JsonObject out(String id, double yield) {
        JsonObject o = new JsonObject();
        JsonUtil.putIfNotNull(o, "id", id);
        JsonUtil.putIfNotNull(o, "yield", yield);
        return o;
    }

    public static JsonArray outs(JsonObject... entries) {
        JsonArray arr = new JsonArray();
        for (JsonObject o : entries) arr.add(o);
        return arr;
    }
}
