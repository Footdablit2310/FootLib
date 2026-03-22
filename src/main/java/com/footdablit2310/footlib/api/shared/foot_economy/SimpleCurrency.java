package com.footdablit2310.footlib.api.shared.foot_economy;

import net.minecraft.world.item.Item;

/**
 * A basic implementation of the Currency interface.
 *
 * Mods can use this class to easily define their own currencies without
 * needing to implement the interface manually.
 */
public class SimpleCurrency implements Currency {

    private final String id;
    private final String name;
    private final Item item;
    private final CurrencyRecipe recipe; // may be null

    public SimpleCurrency(String id, String name, Item item, CurrencyRecipe recipe) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.recipe = recipe;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Item item() {
        return item;
    }

    @Override
    public CurrencyRecipe recipe() {
        return recipe;
    }
}