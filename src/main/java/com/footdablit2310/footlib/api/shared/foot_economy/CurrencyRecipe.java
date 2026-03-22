package com.footdablit2310.footlib.api.shared.foot_economy;

import java.util.Map;

/**
 * A fully flexible recipe descriptor for a currency.
 *
 * This is NOT tied to vanilla crafting. Mods may define any recipe type
 * and provide arbitrary data describing how the recipe works.
 *
 * Foot Economy or other mods can interpret this data however they want.
 */
public class CurrencyRecipe {

    /**
     * The recipe type identifier.
     *
     * Examples:
     * - "minecraft:crafting_shaped"
     * - "minecraft:crafting_shapeless"
     * - "minecraft:smelting"
     * - "foot:minting"
     * - "my_mod:custom_machine"
     */
    private final String type;

    /**
     * Arbitrary key-value data describing the recipe.
     * The meaning of this data depends on the recipe type.
     */
    private final Map<String, Object> data;

    public CurrencyRecipe(String type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public String type() {
        return type;
    }

    public Map<String, Object> data() {
        return data;
    }
}