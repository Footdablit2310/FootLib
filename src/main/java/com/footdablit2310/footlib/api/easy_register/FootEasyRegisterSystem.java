package com.footdablit2310.footlib.api.easy_register;

import com.footdablit2310.footlib.api.easy_register.builders.*;
import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.api.easy_register.config.TabConfig;

import com.footdablit2310.footlib.api.easy_register.types.*;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public final class FootEasyRegisterSystem {

    public final DeferredRegister<Block> blocks;
    public final DeferredRegister<Item> items;
    public final DeferredRegister<BlockEntityType<?>> blockEntities;

    public final DeferredRegister<CreativeModeTab> tabs;
    public final DeferredRegister<FluidType> fluidTypes;
    public final DeferredRegister<Fluid> fluids;
    public final DeferredRegister<MenuType<?>> menus;
    public final DeferredRegister<SoundEvent> sounds;

    private final List<FERCommandEntry> commandEntries = new ArrayList<>();


    public final Map<MenuType<? extends AbstractContainerMenu>, ScreenRegistration<? extends AbstractContainerMenu>> screens = new HashMap<>();

    private final String modId;
    private final String visualName;

    // STRICT: Removed activeTab, visualNameUsed, explicitTabEntries.
    // These violated SRP by making the registry system stateful per-tab.

    // Deferred entries for CUSTOM tabs (keyed by registration name).
    // Entries are merged at registerCreativeTab() time, NOT lazily in a lambda.
    private final Map<String, List<Supplier<ItemStack>>> customTabEntries = new HashMap<>();

    // Deferred entries for EXISTING tabs (vanilla/mod).
    // Applied during BuildCreativeModeTabContentsEvent.
    private final Map<ResourceKey<CreativeModeTab>, List<Supplier<ItemStack>>> existingTabEntries = new HashMap<>();

    public Logger LOGGER;

    // lang datagen
    private final Map<String, String> creativeTabLang = new HashMap<>();


    public FootEasyRegisterSystem(String modId, IEventBus bus, String visualName) {
        this.modId = modId;
        this.visualName = visualName;
        this.LOGGER = FootLib.LOGGER;

        blocks        = DeferredRegister.create(Registries.BLOCK, modId);
        items         = DeferredRegister.create(Registries.ITEM, modId);
        blockEntities = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
        tabs          = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        fluidTypes    = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, modId);
        fluids        = DeferredRegister.create(Registries.FLUID, modId);
        menus         = DeferredRegister.create(Registries.MENU, modId);
        sounds        = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, modId);

        blocks.register(bus);
        items.register(bus);
        blockEntities.register(bus);
        tabs.register(bus);
        fluidTypes.register(bus);
        fluids.register(bus);
        menus.register(bus);
        sounds.register(bus);

        // STRICT: Subscribe to creative tab contents event for existing tabs.
        bus.addListener(this::onBuildContents);
    }

    public String getModId() {
        return modId;
    }

    public String getVisualName() {
        return visualName;
    }

    //datagen lang
    public void registerCreativeTabLang(String key, String value) {
        creativeTabLang.put(key, value);
    }

    public Map<String, String> getCreativeTabLang() { return creativeTabLang; }
    // ------------------------------------------------------------
    // STRICT: Creative Tab Registration
    // ------------------------------------------------------------

    /**
     * Register a custom creative tab from an immutable config.
     * Deferred entries for this tab name are merged BEFORE the registry lambda is captured,
     * preventing DeferredHolder.get() misuse and lambda capture bugs.
     */
    public @NotNull DeferredHolder<CreativeModeTab, CreativeModeTab> registerCreativeTab(@NotNull TabConfig config) {
        // Auto-register lang for translatable titles.
        if (config.title().getContents() instanceof TranslatableContents tc) {
            String key = tc.getKey();
            String value = (this.visualName != null && !this.visualName.isBlank())
                    ? this.visualName
                    : snakeToNormalCase(config.name());
            creativeTabLang.put(key, value);
        }
        List<Supplier<ItemStack>> entries = new ArrayList<>(
                customTabEntries.getOrDefault(config.name(), List.of())
        );

        return tabs.register(config.name(), () -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder().title(config.title());

            if (config.icon() != null) {
                builder = builder.icon(config.icon());
            }

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
                config.displayGenerator().ifPresent(
                    generator -> generator.accept(params, output)
                );
                entries.forEach(s -> output.accept(s.get()));
            });

            return builder.build();
        });
    }

    /**
     * Defer an item to a CUSTOM tab (by registration name).
     * MUST be called BEFORE registerCreativeTab() for that name.
     */
    public void addToTab(String tabName, Supplier<ItemStack> entry) {
        customTabEntries.computeIfAbsent(tabName, k -> new ArrayList<>()).add(entry);
    }

    /**
     * Defer an item to an EXISTING tab (vanilla or another mod).
     * Applied during BuildCreativeModeTabContentsEvent.
     */
    public void addToExistingTab(ResourceKey<CreativeModeTab> tabKey, Supplier<ItemStack> entry) {
        existingTabEntries.computeIfAbsent(tabKey, k -> new ArrayList<>()).add(entry);
    }

    /**
     * NeoForge event handler for populating existing creative tabs.
     */
    @SubscribeEvent
    private void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        List<Supplier<ItemStack>> entries = existingTabEntries.get(event.getTabKey());
        if (entries == null) return;

        entries.forEach(s -> event.accept(s.get()));
    }

    public static String snakeToNormalCase(String snake) {
        if (snake == null || snake.isEmpty()) return snake;

        StringBuilder result = new StringBuilder();
        String[] parts = snake.split("_");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;

            // Capitalize first letter
            result.append(Character.toUpperCase(part.charAt(0)));

            // Append the rest
            if (part.length() > 1) {
                result.append(part.substring(1));
            }

            // Add space between words
            if (i < parts.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    public <T extends Block> FERBlockBuilder<T> block(String name, Supplier<T> factory) {
        return new FERBlockBuilder<>(this, name, factory);
    }

    public <T extends Item> FERItemBuilder<T> item(String name, Supplier<T> factory) {
        return new FERItemBuilder<>(this, name, factory);
    }

    public <T extends BlockEntity> FERBlockEntityBuilder<T> blockEntity(
            String name,
            BlockEntityType.BlockEntitySupplier<T> factory
    ) {
        return new FERBlockEntityBuilder<>(this, name, factory);
    }

    public <T extends AbstractContainerMenu> FERMenuBuilder<T> menu(String name) {
        return new FERMenuBuilder<>(this, name);
    }

    public <S extends FlowingFluid, F extends FlowingFluid> FERFluidBuilder<S, F> fluid(String name) {
        return new FERFluidBuilder<>(this, name);
    }
    public Map<MenuType<? extends AbstractContainerMenu>, ScreenRegistration<? extends AbstractContainerMenu>> getScreens() {
        return this.screens;
    }
    public ScreenRegistration<? extends AbstractContainerMenu> getScreen(
            MenuType<? extends AbstractContainerMenu> menuType) throws IllegalArgumentException{
        ScreenRegistration<?> screen = this.screens.get(menuType);
        if (screen == null) {
            throw new IllegalArgumentException(
                    "No ScreenRegistration found for MenuType: " + menuType
                            + ". This error may be caused by Mixin or FootEasyRegisterSystem.java:addScreen "
                            + "has not been run or duplicate FER instances."
            );
        }
        return screen;
    }
    public void addScreen(ScreenRegistration<? extends AbstractContainerMenu> reg) {
        this.screens.put(reg.menuType(), reg);
    }
    public <ETACM extends AbstractContainerMenu> FERMenuLinkedScreenBuilder<ETACM> screen(MenuType<ETACM> menuType) {
        return new FERMenuLinkedScreenBuilder<>(this, menuType);
    }

    public void addCommand(FERCommandEntry entry) {
        commandEntries.add(entry);
    }
    public FERCommandBuilder command(CommandBuildContext context) {
        return new FERCommandBuilder(this, context);
    }
    public FERCommandBuilder command(String namespace, CommandBuildContext context) {
        return new FERCommandBuilder(this, namespace, context);
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        for (FERCommandEntry entry : commandEntries) {
            event.getDispatcher().register(entry.build());
        }
    }

    public FERSoundEventBuilder soundEvent() {
        return new FERSoundEventBuilder(this);
    }

}