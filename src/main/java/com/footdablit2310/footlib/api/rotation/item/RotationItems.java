package com.footdablit2310.footlib.api.rotation.item;

import com.footdablit2310.footlib.FootLib;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;

/** Registration for FootLib's seven quaternion rotation tools. */
public final class RotationItems {
    public static DeferredHolder<Item, RotationToolItem> COPPER;
    public static DeferredHolder<Item, RotationToolItem> IRON;
    public static DeferredHolder<Item, RotationToolItem> GOLD;
    public static DeferredHolder<Item, RotationToolItem> AMETHYST;
    public static DeferredHolder<Item, RotationToolItem> DIAMOND;
    public static DeferredHolder<Item, RotationToolItem> NETHERITE;
    public static DeferredHolder<Item, RotationToolItem> ULTRA_NETHERITE;

    private RotationItems() {}

    public static void register() {
        COPPER = register("rotation_tool_copper", 22.5, 1024);
        IRON = register("rotation_tool_iron", 11.25, 2048);
        GOLD = register("rotation_tool_gold", 5.0, 512);
        AMETHYST = register("rotation_tool_amethyst", 2.5, 4096);
        DIAMOND = register("rotation_tool_diamond", 1.0, 8192);
        NETHERITE = register("rotation_tool_netherite", 0.1, 16384);
        ULTRA_NETHERITE = register("rotation_tool_ultra_netherite", 1.0E-7, 32768);
    }

    private static DeferredHolder<Item, RotationToolItem> register(String name, double precision, int durability) {
        return FootLib.FER.item(name,
                () -> new RotationToolItem(new Item.Properties().stacksTo(1).durability(durability), precision))
                .creativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES).register();
    }
}
