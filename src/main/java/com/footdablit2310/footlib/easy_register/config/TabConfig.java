package com.footdablit2310.footlib.easy_register.config;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

/**
 * Immutable configuration for a creative tab. No registration logic.
 */
public record TabConfig(
        String name,
        Component title,
        Supplier<ItemStack> icon,
        boolean searchBar,
        int searchBarWidth,
        List<ResourceLocation> beforeTabs,
        List<ResourceLocation> afterTabs,
        CreativeModeTab.DisplayItemsGenerator displayGenerator
) {
    public TabConfig {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tab name must not be blank");
        }
        if (title == null) {
            throw new IllegalArgumentException("Tab title must not be null");
        }
        if (icon == null) {
            throw new IllegalArgumentException("Tab icon must not be null");
        }
        // Defensive copies
        beforeTabs = List.copyOf(beforeTabs);
        afterTabs = List.copyOf(afterTabs);
    }
}