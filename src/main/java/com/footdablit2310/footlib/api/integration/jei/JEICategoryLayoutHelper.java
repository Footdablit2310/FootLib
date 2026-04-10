package com.footdablit2310.footlib.api.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class JEICategoryLayoutHelper {

    private JEICategoryLayoutHelper() {}

    public static void basicItemInOut(
            IRecipeLayoutBuilder builder,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        int xIn = 20;
        int yIn = 20;
        int xOut = 100;
        int yOut = 20;

        for (ItemStack in : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, xIn, yIn)
                   .addItemStack(in);
            xIn += 18;
        }

        for (ItemStack out : outputs) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOut, yOut)
                   .addItemStack(out);
            xOut += 18;
        }
    }

    public static void applyFocus(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // Optional: focus-aware behavior
    }
}
