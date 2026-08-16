package com.footdablit2310.footlib.easy_register;

import com.footdablit2310.footlib.easy_register.config.TabConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

public final class CreativeTabRegistrar {
    private final DeferredRegister<CreativeModeTab> tabs;
    private final String modId;

    // Deferred entries for CUSTOM tabs (keyed by tab NAME, not holder)
    private final Map<String, List<Supplier<ItemStack>>> customTabEntries = new HashMap<>();

    // Deferred entries for EXISTING tabs (keyed by CreativeModeTab instance)
    private final Map<CreativeModeTab, List<Supplier<ItemStack>>> existingTabEntries = new IdentityHashMap<>();

    public CreativeTabRegistrar(String modId, IEventBus modBus) {
        this.modId = modId;
        this.tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        this.tabs.register(modBus);
        modBus.addListener(this::onBuildContents);
    }

    /**
     * Register a custom tab from a config. All deferred entries for this tab name
     * are merged BEFORE the lambda is captured.
     */
    public Supplier<CreativeModeTab> register(TabConfig config) {
        // Merge deferred entries NOW, before the lambda captures the list
        List<Supplier<ItemStack>> entries = new ArrayList<>(
                customTabEntries.getOrDefault(config.name(), List.of())
        );

        return tabs.register(config.name(), () -> {
            var builder = CreativeModeTab.builder()
                    .title(config.title())
                    .icon(config.icon());

            if (config.searchBar()) {
                builder = builder.withSearchBar(config.searchBarWidth());
            }
            if (!config.beforeTabs().isEmpty()) {
                builder = builder.withTabsBefore(config.beforeTabs().toArray(ResourceLocation[]::new));
            }
            if (!config.afterTabs().isEmpty()) {
                builder = builder.withTabsAfter(config.afterTabs().toArray(ResourceLocation[]::new));
            }

            builder = builder.displayItems((params, output) -> {
                if (config.displayGenerator() != null) {
                    config.displayGenerator().accept(params, output);
                }
                entries.forEach(s -> output.accept(s.get()));
            });

            return builder.build();
        });
    }

    /**
     * Defer an item to a CUSTOM tab (by name). Must be called BEFORE register().
     */
    public void deferToCustomTab(String tabName, Supplier<ItemStack> entry) {
        customTabEntries.computeIfAbsent(tabName, k -> new ArrayList<>()).add(entry);
    }

    /**
     * Defer an item to an EXISTING tab (by CreativeModeTab key).
     * These are applied during BuildCreativeModeTabContentsEvent.
     */
    public void deferToExistingTab(CreativeModeTab tab, Supplier<ItemStack> entry) {
        existingTabEntries.computeIfAbsent(tab, k -> new ArrayList<>()).add(entry);
    }

    /**
     * NeoForge event: populate existing tabs.
     */
    private void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        List<Supplier<ItemStack>> entries = existingTabEntries.get(event.getTab());
        if (entries == null) return;

        entries.forEach(s -> event.accept(s.get()));
    }
}