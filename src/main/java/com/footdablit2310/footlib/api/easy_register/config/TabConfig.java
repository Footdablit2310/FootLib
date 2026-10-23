package com.footdablit2310.footlib.api.easy_register.config;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Immutable configuration for a creative tab. No registration logic.
 * For default searchbar size pass a number below 0 or exactly 89 as the value.
 */
public class TabConfig {
    private String name;
    private Component title;
    private Supplier<ItemStack> icon;
    private boolean searchBar;
    private int searchBarWidth;
    private List<ResourceLocation> beforeTabs;
    private List<ResourceLocation> afterTabs;
    private CreativeModeTab.DisplayItemsGenerator displayGenerator;

    public TabConfig(
        @NonNull String name,
        @NonNull Component title,
        Optional<Supplier<ItemStack>> icon,
        boolean searchBar,
        int searchBarWidth,
        Optional<List<ResourceLocation>> beforeTabs,
        Optional<List<ResourceLocation>> afterTabs,
        Optional<CreativeModeTab.DisplayItemsGenerator> displayGenerator
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tab name must not be blank");
        }
        if (title == null || title.getString().isBlank() || title.getString() == null) {
            throw new IllegalArgumentException("Tab title must not be null");
        }
        if (searchBarWidth <= 0) {
            searchBarWidth = 89;
        }
        // Defensive copies
        if (afterTabs.isEmpty()) {
            this.afterTabs = null;
        } else { this.afterTabs = List.copyOf(afterTabs.get()); }
        if (beforeTabs.isEmpty()) {
            this.beforeTabs = null;
        } else { this.beforeTabs = List.copyOf(beforeTabs.get()); }

    }


	public String name() {
		return name;
	}

    public boolean searchBar() {
        return searchBar;
    }

    public Component title() {
        return title;
    }

    public CreativeModeTab.DisplayItemsGenerator displayGenerator() {
        return displayGenerator;
    }

    public int searchBarWidth() {
        return searchBarWidth;
    }

    public List<ResourceLocation> afterTabs() {
        return afterTabs;
    }

    public List<ResourceLocation> beforeTabs() {
        return beforeTabs;
    }

    public Supplier<ItemStack> icon() {
        return icon;
    }
}