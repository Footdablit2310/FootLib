package com.footdablit2310.footlib.api.integration.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class JEIRecipeRegistry {
    private static final Map<ResourceLocation, List<Supplier<JEIRecipeWrapper>>> RECIPES = new LinkedHashMap<>();

    private JEIRecipeRegistry() {}

    public static void register(
            ResourceLocation type,
            Supplier<? extends List<ItemStack>> inputs,
            Supplier<? extends List<ItemStack>> outputs
    ) {
        Objects.requireNonNull(type, "Recipe type cannot be null");
        Objects.requireNonNull(inputs, "Recipe inputs supplier cannot be null");
        Objects.requireNonNull(outputs, "Recipe outputs supplier cannot be null");

        synchronized (RECIPES) {
            RECIPES.computeIfAbsent(type, ignored -> new ArrayList<>())
                    .add(() -> new JEIRecipeWrapper(inputs.get(), outputs.get()));
        }
    }

    public static Map<ResourceLocation, List<JEIRecipeWrapper>> getRecipes() {
        Map<ResourceLocation, List<Supplier<JEIRecipeWrapper>>> snapshot = new LinkedHashMap<>();

        synchronized (RECIPES) {
            RECIPES.forEach((type, entries) -> snapshot.put(type, List.copyOf(entries)));
        }

        Map<ResourceLocation, List<JEIRecipeWrapper>> recipes = new LinkedHashMap<>();
        snapshot.forEach((type, entries) ->
                recipes.put(type, entries.stream().map(Supplier::get).toList()));
        return Map.copyOf(recipes);
    }
}
