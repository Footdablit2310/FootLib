package com.footdablit2310.footlib.api.shared.foot_economy;

import net.minecraft.world.item.Item;

public interface Currency {

    String id();

    String name();

    Item item();

    /**
     * @return the crafting recipe descriptor, or null if none exists.
     */
    CurrencyRecipe recipe();
}