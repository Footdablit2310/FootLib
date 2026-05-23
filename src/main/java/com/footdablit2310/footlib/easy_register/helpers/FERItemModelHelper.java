package com.footdablit2310.footlib.easy_register.helpers;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public final class FERItemModelHelper {

    private FERItemModelHelper() {}

    public static void generated(ItemModelProvider prov, Item item) {
        prov.basicItem(item);
    }
}
