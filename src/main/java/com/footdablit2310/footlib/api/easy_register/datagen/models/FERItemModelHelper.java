package com.footdablit2310.footlib.api.easy_register.datagen.models;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public final class FERItemModelHelper {

    private FERItemModelHelper() {}

    public static void generated(ItemModelProvider provider, Item item) {
        provider.basicItem(item);
    }
}
