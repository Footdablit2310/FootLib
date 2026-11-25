package com.footdablit2310.footlib.api.rotation.item;

import com.footdablit2310.footlib.FootLib;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/** Registration for FootLib's built-in rotation tool. */
public final class RotationItems {
    public static DeferredHolder<Item, RotationToolItem> ROTATION_TOOL;

    private RotationItems() {
    }

    public static void register() {
        ROTATION_TOOL = FootLib.FER.item(
                "rotation_tool",
                () -> new RotationToolItem(new Item.Properties().stacksTo(1))
        ).register();
    }
}
