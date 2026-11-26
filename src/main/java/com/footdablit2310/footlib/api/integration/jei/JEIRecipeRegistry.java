package com.footdablit2310.footlib.api.integration.jei;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Common-side storage for recipe information declared through FER builders.
 * The JEI plugin reads this data on the client and converts it to JEI recipes.
 */
public final class JEIRecipeRegistry {

    private static final List<Supplier<JEIRecipeData>> RECIPES = new CopyOnWriteArrayList<>();

    private JEIRecipeRegistry() {}

    public static void register(Supplier<JEIRecipeData> recipe) {
        RECIPES.add(recipe);
    }

    public static List<JEIRecipeData> getRecipes() {
        return RECIPES.stream().map(Supplier::get).toList();
    }
}
