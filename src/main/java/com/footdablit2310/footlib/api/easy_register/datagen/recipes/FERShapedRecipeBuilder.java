package com.footdablit2310.footlib.api.easy_register.datagen.recipes;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

public final class FERShapedRecipeBuilder {

    private FERShapedRecipeBuilder() {}

    public static ShapedRecipeBuilder shaped(ItemLike result) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result);
    }

    public static ShapedRecipeBuilder shaped(ItemLike result, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count);
    }
}
