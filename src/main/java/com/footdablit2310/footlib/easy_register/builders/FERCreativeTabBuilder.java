package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
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
    private int searchBarWidth = 80;

    private final List<ResourceLocation> beforeTabs = new ArrayList<>();
    private final List<ResourceLocation> afterTabs = new ArrayList<>();

    private CreativeModeTab.DisplayItemsGenerator displayOverride = null;
    private final List<Supplier<ItemStack>> entries = new ArrayList<>();

    @SuppressWarnings("unused")
    private String langKey = null;
    @SuppressWarnings("unused")
    private String langValue = null;

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

        boolean hasExplicitTitle = (this.title != null);
        boolean hasVisualName    = (reg.getVisualName() != null && !reg.getVisualName().isBlank());

        Component finalTitle;

        // ------------------------------------------
        // CASE 1 — Explicit title always wins
        // ------------------------------------------
        if (hasExplicitTitle) {
            finalTitle = this.title;
        }

        // ------------------------------------------
        // CASE 2 — No explicit title, visualName exists
        // ------------------------------------------
        else if (hasVisualName) {

            // Check if visualName already used by another tab
            if (reg.isVisualNameUsed()) {
                throw new IllegalStateException(
                    "Creative tab '" + name + "' attempted to use visualName '" +
                    reg.getVisualName() + "', but it is already used by another tab."
                );
            }

            reg.LOGGER.warn(
                "No title provided for creative tab '{}', using visualName='{}'",
                name, reg.getVisualName()
            );

            reg.markVisualNameUsed(); // mark as consumed

            finalTitle = Component.literal(reg.getVisualName());
        }

        // ------------------------------------------
        // CASE 3 — No title, no visualName → fallback
        // ------------------------------------------
        else {
            finalTitle = Component.translatable("itemGroup." + reg.getModId() + "." + name);
        }

        // Case 1: Title is translatable
        if (finalTitle.getContents() instanceof TranslatableContents tc) {
            langKey = tc.getKey();
            langValue = reg.getVisualName() != null ? reg.getVisualName() : name;
        }

        // Case 2: Title is literal
        else {
            langKey = "itemGroup." + reg.getModId() + "." + name;
            langValue = finalTitle.getString();
        }

        // ------------------------------------------
        // REGISTER TAB
        // ------------------------------------------

        DeferredHolder<CreativeModeTab, CreativeModeTab> holder =
            reg.tabs.register(name, () -> {

                CreativeModeTab.Builder builder = CreativeModeTab.builder()
                        .title(finalTitle)
                        .icon(icon);

                if (enableSearchBar) {
                    builder = builder.withSearchBar(searchBarWidth);
                }

                if (!beforeTabs.isEmpty()) {
                    builder = builder.withTabsBefore(beforeTabs.toArray(ResourceLocation[]::new));
                }
                if (!afterTabs.isEmpty()) {
                    builder = builder.withTabsAfter(afterTabs.toArray(ResourceLocation[]::new));
                }

                builder = builder.displayItems((params, output) -> {
                    if (displayOverride != null) {
                        displayOverride.accept(params, output);
                    } else {
                        entries.forEach(s -> output.accept(s.get()));
                    }
                });

                return builder.build();
            });
        // Merge explicit entries for this tab
        List<Supplier<ItemStack>> extra = reg.explicitTabEntries.get(holder.get());
        if (extra != null) {
            extra.forEach(entries::add);
        }

        reg.setActiveCreativeTab(this);
        return holder;
    }

}
