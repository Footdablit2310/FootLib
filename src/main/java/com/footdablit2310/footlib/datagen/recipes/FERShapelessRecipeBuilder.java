package com.footdablit2310.footlib.datagen.recipes;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;

public final class FERShapelessRecipeBuilder {

    private FERShapelessRecipeBuilder() {}

    public static ShapelessRecipeBuilder shapeless(ItemLike result) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result);
    }

    public static ShapelessRecipeBuilder shapeless(ItemLike result, int count) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count);
    }
}
