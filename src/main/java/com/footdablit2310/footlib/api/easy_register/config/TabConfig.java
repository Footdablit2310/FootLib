package com.footdablit2310.footlib.api.easy_register.config;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Immutable configuration for a creative tab. No registration logic.
 * For default searchbar size pass a number below 0 or exactly 89 as the value.
 */
public class TabConfig {
    private final @NonNull String name;
    private final @NonNull Component title;
    private final @NonNull Supplier<ItemStack> icon;
    private final boolean searchBar;
    private final int searchBarWidth;
    private final List<ResourceLocation> beforeTabs;
    private final List<ResourceLocation> afterTabs;
    private final Optional<CreativeModeTab.DisplayItemsGenerator> displayGenerator;

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
        if (title == null) {
            throw new IllegalArgumentException("Tab title must not be null");
        }

        if (title.getString().isBlank()) {
            throw new IllegalArgumentException("Tab title must not be blank");
        }

        if (searchBarWidth <= 0) {
            searchBarWidth = 89;
        }

        this.name = name;
        this.title = title;
        this.searchBarWidth = searchBarWidth;
        this.searchBar = searchBar;
	    this.icon = icon.orElse(Items.COPPER_INGOT::getDefaultInstance);
        // Defensive copies
        this.afterTabs = afterTabs.map(List::copyOf).orElse(List.of());
        this.beforeTabs = beforeTabs.map(List::copyOf).orElse(List.of());
        this.displayGenerator = displayGenerator;
    }


	public String name() {
		return name;
	}

    public boolean searchBar() {
        return searchBar;
    }

    public @NonNull Component title() {
        return title;
    }

    public Optional<CreativeModeTab.DisplayItemsGenerator> displayGenerator() {
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