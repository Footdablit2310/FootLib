package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.config.TabConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class FERCreativeTabBuilder {
    private String name;
    private Component title;
    private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
    private boolean searchBar = false;
    private int searchBarWidth = 80;
    private final List<ResourceLocation> beforeTabs = new ArrayList<>();
    private final List<ResourceLocation> afterTabs = new ArrayList<>();
    private CreativeModeTab.DisplayItemsGenerator displayGenerator;

    public FERCreativeTabBuilder name(String name) {
        this.name = name;
        return this;
    }
    public FERCreativeTabBuilder(String name) {
        this.name = name;
    }

    public FERCreativeTabBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public FERCreativeTabBuilder icon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public FERCreativeTabBuilder searchBar() {
        this.searchBar = true;
        return this;
    }

    public FERCreativeTabBuilder searchBar(int width) {
        this.searchBar = true;
        this.searchBarWidth = width;
        return this;
    }

    public FERCreativeTabBuilder before(ResourceLocation... tabs) {
        beforeTabs.addAll(List.of(tabs));
        return this;
    }

    public FERCreativeTabBuilder after(ResourceLocation... tabs) {
        afterTabs.addAll(List.of(tabs));
        return this;
    }

    public FERCreativeTabBuilder displayGenerator(CreativeModeTab.DisplayItemsGenerator generator) {
        this.displayGenerator = generator;
        return this;
    }

    /**
     * Builds an immutable TabConfig. Zero side effects.
     */
    public TabConfig build() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Tab name is required. Call .name() before .build()");
        }
        if (title == null) {
            throw new IllegalStateException("Tab title is required. Call .title() before .build()");
        }
        return new TabConfig(
                name,
                title,
                icon,
                searchBar,
                searchBarWidth,
                List.copyOf(beforeTabs),
                List.copyOf(afterTabs),
                displayGenerator
        );
    }
}