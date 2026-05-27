package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class FERCreativeTabBuilder {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
    private Component title = null;

    private boolean enableSearchBar = false;
    private int searchBarWidth = 80; // NeoForge default

    private final List<ResourceLocation> beforeTabs = new ArrayList<>();
    private final List<ResourceLocation> afterTabs = new ArrayList<>();

    private CreativeModeTab.DisplayItemsGenerator displayOverride = null;
    private final List<Supplier<ItemStack>> entries = new ArrayList<>();


    public FERCreativeTabBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    // -------------------------------
    // Configuration
    // -------------------------------

    public FERCreativeTabBuilder icon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public FERCreativeTabBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public FERCreativeTabBuilder searchBar() {
        this.enableSearchBar = true;
        return this;
    }

    public FERCreativeTabBuilder searchBar(int width) {
        this.enableSearchBar = true;
        this.searchBarWidth = width;
        return this;
    }

    public FERCreativeTabBuilder before(ResourceLocation... tabs) {
        for (var t : tabs) beforeTabs.add(t);
        return this;
    }

    public FERCreativeTabBuilder after(ResourceLocation... tabs) {
        for (var t : tabs) afterTabs.add(t);
        return this;
    }

    public FERCreativeTabBuilder displayOverride(CreativeModeTab.DisplayItemsGenerator generator) {
        this.displayOverride = generator;
        return this;
    }

    public FERCreativeTabBuilder add(Supplier<ItemStack> stack) {
        entries.add(stack);
        return this;
    }

    // -------------------------------
    // Registration
    // -------------------------------

    public DeferredHolder<CreativeModeTab, CreativeModeTab> register() {

        Component finalTitle = (title != null)
                ? title
                : Component.translatable("itemGroup." + reg.getModId() + "." + name);

        return reg.tabs.register(name, () -> {

            CreativeModeTab.Builder builder = CreativeModeTab.builder()
                    .title(finalTitle)
                    .icon(icon);

            // Search bar (NeoForge requires width)
            if (enableSearchBar) {
                builder = builder.withSearchBar(searchBarWidth);
            }

            // Ordering
            if (!beforeTabs.isEmpty()) {
                builder = builder.withTabsBefore(beforeTabs.toArray(ResourceLocation[]::new));
            }
            if (!afterTabs.isEmpty()) {
                builder = builder.withTabsAfter(afterTabs.toArray(ResourceLocation[]::new));
            }

            // Items
            builder = builder.displayItems((params, output) -> {
                if (displayOverride != null) {
                    displayOverride.accept(params, output);
                } else {
                    entries.forEach(s -> output.accept(s.get()));
                }
            });

            return builder.build();
        });
    }
}
