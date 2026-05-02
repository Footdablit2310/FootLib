package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.ItemJsonUtil;
import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CreateSequencedAssemblyJsonUtil extends RecipeJsonBuilderUtil {

    private final JsonArray sequence = new JsonArray();
    private final JsonArray results = new JsonArray();

    private CreateSequencedAssemblyJsonUtil() {
        type("create:sequenced_assembly");
    }

    public static CreateSequencedAssemblyJsonUtil create() {
        return new CreateSequencedAssemblyJsonUtil();
    }

    public CreateSequencedAssemblyJsonUtil input(String itemId) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", itemId);
        root.add("ingredient", ingredient);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil inputTag(String tagId) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", tagId);
        root.add("ingredient", ingredient);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil transitionalItem(String itemId) {
        JsonObject trans = new JsonObject();
        trans.addProperty("item", itemId);
        root.add("transitionalItem", trans);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil addStep(JsonObject step) {
        sequence.add(step);
        root.add("sequence", sequence);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil loops(int loops) {
        root.addProperty("loops", loops);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil addResult(String itemId, float chance) {
        JsonObject result = new JsonObject();
        result.addProperty("item", itemId);
        result.addProperty("chance", chance);
        results.add(result);
        root.add("results", results);
        return this;
    }

    public CreateSequencedAssemblyJsonUtil addPartialResult(String itemId, int count, float chance) {
        JsonObject result = ItemJsonUtil.item(itemId, count);
        result.addProperty("chance", chance);
        results.add(result);
        root.add("results", results);
        return this;
    }

    // Common step helpers
    public static JsonObject deployingStep(String heldItemId) {
        JsonObject step = new JsonObject();
        step.addProperty("type", "create:deploying");

        JsonArray ingredients = new JsonArray();
        JsonObject held = new JsonObject();
        held.addProperty("item", heldItemId);
        ingredients.add(held);

        step.add("ingredients", ingredients);
        return step;
    }

    public static JsonObject pressingStep() {
        JsonObject step = new JsonObject();
        step.addProperty("type", "create:pressing");
        return step;
    }
}
