package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.ItemJsonUtil;
import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    public CreateMechanicalCraftingJsonUtil defineItem(char symbol, String itemId, int count) {
        key.add(String.valueOf(symbol), ItemJsonUtil.item(itemId, count));
        root.add("key", key);
        return this;
    }

    public CreateMechanicalCraftingJsonUtil defineTag(char symbol, String tagId, int count) {
        key.add(String.valueOf(symbol), ItemJsonUtil.itemTag(tagId, count));
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
