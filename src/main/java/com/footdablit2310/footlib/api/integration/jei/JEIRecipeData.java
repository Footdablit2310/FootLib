package com.footdablit2310.footlib.api.integration.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * JEI recipe data that can be declared from common item and block builders.
 * This class deliberately has no dependency on JEI API classes.
 */
public record JEIRecipeData(
        ResourceLocation recipeType,
        List<ItemStack> inputs,
        List<ItemStack> outputs
) {
    public JEIRecipeData {
        Objects.requireNonNull(recipeType, "recipeType");
        inputs = copyStacks(inputs, "inputs");
        outputs = copyStacks(outputs, "outputs");
    }

    private static List<ItemStack> copyStacks(List<ItemStack> stacks, String name) {
        Objects.requireNonNull(stacks, name);
        return stacks.stream()
                .map(stack -> Objects.requireNonNull(stack, name + " contains null").copy())
                .toList();
    }
}
