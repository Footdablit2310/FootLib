package com.footdablit2310.footlib.api.shared.foot_organic_processing;

import com.google.gson.JsonArray;
import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

/**
 * JSON builder for the FootOrganicProcessing PTF Heated recipe type.
 * This belongs in footlib/api/shared/<mod_id> because it is a utility
 * for your own mod's recipe format, not an integration layer.
 */
public class PTFHeatedJsonUtil extends RecipeJsonBuilderUtil {

    private PTFHeatedJsonUtil() {
        type("footorganicprocessing:ptf_heated");
    }

    public static PTFHeatedJsonUtil create() {
        return new PTFHeatedJsonUtil();
    }

    /**
     * Sets the heat tier for this recipe.
     * Valid tiers:
     * - heated
     * - superheated
     * - ultraheated (custom, FootOP-only)
     */
    public PTFHeatedJsonUtil heat(String tier) {
        root.addProperty("heat", tier);
        return this;
    }

    /**
     * Sets the ingredient array.
     * Uses the unified DSL: inputsOf(), item(), itemTag(), fluidInput(), etc.
     */
    public PTFHeatedJsonUtil ingredients(JsonArray arr) {
        root.add("ingredients", arr);
        return this;
    }

    /**
     * Sets the results array.
     * Uses the unified DSL: outs(), out(), outCount(), etc.
     */
    public PTFHeatedJsonUtil results(JsonArray arr) {
        root.add("results", arr);
        return this;
    }
}
