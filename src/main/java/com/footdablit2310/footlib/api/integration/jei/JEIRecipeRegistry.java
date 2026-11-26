package com.footdablit2310.footlib.api.integration.jei;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Common-side storage for recipe information declared through FER builders.
 * The JEI plugin reads this data on the client and converts it to JEI recipes.
 */
public final class JEIRecipeRegistry {

    private static final List<JEIRecipeData> RECIPES = new CopyOnWriteArrayList<>();

    private JEIRecipeRegistry() {}

    public static void register(JEIRecipeData recipe) {
        RECIPES.add(recipe);
    }

    public static List<JEIRecipeData> getRecipes() {
        return List.copyOf(RECIPES);
    }
}
