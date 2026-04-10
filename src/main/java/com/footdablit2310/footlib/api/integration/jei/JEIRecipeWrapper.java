package com.footdablit2310.footlib.api.integration.jei;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public record JEIRecipeWrapper(
        List<ItemStack> inputs,
        List<ItemStack> outputs
) {}
