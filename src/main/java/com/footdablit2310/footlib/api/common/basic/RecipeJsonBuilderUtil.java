package com.footdablit2310.footlib.api.common.basic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RecipeJsonBuilderUtil {

    protected final JsonObject root = new JsonObject();

    public RecipeJsonBuilderUtil type(String type) {
        root.addProperty("type", type);
        return this;
    }

    /* ------------------------------------------------------------
     * ITEM INPUTS
     * ------------------------------------------------------------ */

    public RecipeJsonBuilderUtil itemInput(String itemId, int count) {
        JsonObject in = new JsonObject();
        in.addProperty("item", itemId);
        if (count > 1) in.addProperty("count", count);
        root.add("input", in);
        return this;
    }

    public RecipeJsonBuilderUtil itemTagInput(String tagId, int count) {
        JsonObject in = new JsonObject();
        in.addProperty("tag", tagId);
        if (count > 1) in.addProperty("count", count);
        root.add("input", in);
        return this;
    }

    /* ------------------------------------------------------------
     * MULTIPLE INPUTS (Create mixing, filling, etc.)
     * ------------------------------------------------------------ */

    public RecipeJsonBuilderUtil inputs(JsonArray arr) {
        root.add("ingredients", arr);
        return this;
    }

    public static JsonArray inputsOf(JsonObject... objs) {
        JsonArray arr = new JsonArray();
        for (JsonObject o : objs) arr.add(o);
        return arr;
    }

    /* ------------------------------------------------------------
     * FLUID INPUTS
     * ------------------------------------------------------------ */

    public RecipeJsonBuilderUtil fluidInput(String fluidId, int amount) {
        JsonObject f = new JsonObject();
        f.addProperty("fluid", fluidId);
        f.addProperty("amount", amount);
        root.add("fluid", f);
        return this;
    }

    public RecipeJsonBuilderUtil fluidTagInput(String tagId, int amount) {
        JsonObject f = new JsonObject();
        JsonObject tag = new JsonObject();
        tag.addProperty("tag", tagId);
        f.add("fluidTag", tag);
        f.addProperty("amount", amount);
        root.add("fluid", f);
        return this;
    }

    /* ------------------------------------------------------------
     * OUTPUTS
     * ------------------------------------------------------------ */

    public RecipeJsonBuilderUtil outputs(JsonArray arr) {
        root.add("results", arr);
        return this;
    }

    public static JsonObject out(String itemId, double chance) {
        JsonObject o = new JsonObject();
        o.addProperty("item", itemId);
        o.addProperty("chance", chance);
        return o;
    }

    public static JsonObject outCount(String itemId, int count, double chance) {
        JsonObject o = new JsonObject();
        o.addProperty("item", itemId);
        o.addProperty("count", count);
        o.addProperty("chance", chance);
        return o;
    }

    public static JsonArray outs(JsonObject... objs) {
        JsonArray arr = new JsonArray();
        for (JsonObject o : objs) arr.add(o);
        return arr;
    }

    /* ------------------------------------------------------------
     * BUILD
     * ------------------------------------------------------------ */

    public JsonObject build() {
        return root;
    }
}
