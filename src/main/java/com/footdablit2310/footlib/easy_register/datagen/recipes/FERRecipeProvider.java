package com.footdablit2310.footlib.easy_register.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public abstract class FERRecipeProvider extends RecipeProvider {

    protected final CompletableFuture<HolderLookup.Provider> registries;

    public FERRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.registries = registries;
    }
}
