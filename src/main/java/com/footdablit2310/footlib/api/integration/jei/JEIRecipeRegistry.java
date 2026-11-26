package com.footdablit2310.footlib.api.integration.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class JEIRecipeRegistry {
    private static final Map<ResourceLocation, List<JEIRecipeWrapper>> RECIPES = new LinkedHashMap<>();

    private JEIRecipeRegistry() {}

    public static void register(ResourceLocation type, List<ItemStack> inputs, List<ItemStack> outputs) {
        RECIPES.computeIfAbsent(type, ignored -> new ArrayList<>())
                .add(new JEIRecipeWrapper(inputs, outputs));
    }

    public static Map<ResourceLocation, List<JEIRecipeWrapper>> getRecipes() {
        return Map.copyOf(RECIPES);
    }
}
