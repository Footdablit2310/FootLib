package com.footdablit2310.footlib.api.common.basic;

import com.google.gson.JsonObject;

public final class FluidJsonUtil {

    private FluidJsonUtil() {}

    public static JsonObject fluid(String fluidId, int amount) {
        JsonObject obj = new JsonObject();
        obj.addProperty("fluid", fluidId);
        obj.addProperty("amount", amount);
        return obj;
    }

    public static JsonObject fluidTag(String tagId, int amount) {
        JsonObject obj = new JsonObject();
        JsonObject tag = new JsonObject();
        tag.addProperty("tag", tagId);
        obj.add("fluidTag", tag);
        obj.addProperty("amount", amount);
        return obj;
    }
}
