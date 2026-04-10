package com.footdablit2310.footlib.api.integration.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateMechanicalCraftingJsonUtil extends RecipeJsonBuilderUtil {

    private final JsonArray pattern = new JsonArray();
    private final JsonObject key = new JsonObject();

    private CreateMechanicalCraftingJsonUtil() {
        type("create:mechanical_crafting");
    }

    public static CreateMechanicalCraftingJsonUtil create() {
        return new CreateMechanicalCraftingJsonUtil();
    }

    public CreateMechanicalCraftingJsonUtil patternLine(String line) {
        pattern.add(line);
        root.add("pattern", pattern);
        return this;
    }

    public CreateMechanicalCraftingJsonUtil define(char symbol, String itemId) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", itemId);
        key.add(String.valueOf(symbol), ingredient);
        root.add("key", key);
        return this;
    }

    public CreateMechanicalCraftingJsonUtil result(String itemId, int count) {
        JsonObject result = new JsonObject();
        result.addProperty("item", itemId);
        result.addProperty("count", count);
        root.add("result", result);
        return this;
    }
}
