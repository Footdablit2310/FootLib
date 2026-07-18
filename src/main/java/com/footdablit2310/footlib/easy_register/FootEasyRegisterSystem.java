package com.footdablit2310.footlib.easy_register;

import com.footdablit2310.footlib.easy_register.builders.*;
import com.footdablit2310.footlib.FootLib;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
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
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.tags.TagKey;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FootEasyRegisterSystem {

    public final DeferredRegister<Block> blocks;
    public final DeferredRegister<Item> items;
    public final DeferredRegister<BlockEntityType<?>> blockEntities;

    public final DeferredRegister<CreativeModeTab> tabs;
    public final DeferredRegister<FluidType> fluidTypes;
    public final DeferredRegister<Fluid> fluids;
    public final DeferredRegister<MenuType<?>> menus;

    private final String modId;
    private final String visualName;

    // active creative tab for auto‑add from builders
    private FERCreativeTabBuilder activeTab;
    private boolean visualNameUsed = false;
    public Logger LOGGER;

    //lang datagen
    private final Map<String, String> creativeTabLang = new HashMap<>();
    // Language
    private final Map<String, String> langEntries = new HashMap<>();

    // Blockstates
    private final Map<Identifier, Block> blockstateEntries = new HashMap<>();

    // Item models
    private final Map<Identifier, Item> itemModelEntries = new HashMap<>();

    // Loot tables
    private final Map<Identifier, Block> lootEntries = new HashMap<>();

    // Tags
    private final java.util.Map<TagKey<Block>, java.util.List<Block>> blockTagEntries = new java.util.HashMap<>();
    private final java.util.Map<TagKey<Item>, java.util.List<Item>> itemTagEntries = new java.util.HashMap<>();
    private final java.util.Map<TagKey<Fluid>, java.util.List<Fluid>> fluidTagEntries = new java.util.HashMap<>();
    private final Map<TagKey<Block>, TagKey<Item>> copiedBlockTags = new HashMap<>();

    // Recipes
    private final List<Consumer<RecipeOutput>> recipeEntries = new ArrayList<>();

    public FootEasyRegisterSystem(String modId, IEventBus bus, String visualName) {
        this.modId = modId;
        this.visualName = visualName;
        this.LOGGER = FootLib.LOGGER;

        blocks = DeferredRegister.create(Registries.BLOCK, modId);
        items = DeferredRegister.create(Registries.ITEM, modId);
        blockEntities = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
        tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        fluidTypes = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, modId);
        fluids = DeferredRegister.create(Registries.FLUID, modId);
        menus = DeferredRegister.create(Registries.MENU, modId);

        blocks.register(bus);
        items.register(bus);
        blockEntities.register(bus);
        tabs.register(bus);
        fluidTypes.register(bus);
        fluids.register(bus);
        menus.register(bus);
    }

    public String getModId() {
        return modId;
    }

    public String getVisualName() {
        return visualName;
    }

    public boolean isVisualNameUsed() {
        return visualNameUsed;
    }

    public void markVisualNameUsed() {
        this.visualNameUsed = true;
    }

    //datagen lang
    public void registerCreativeTabLang(String key, String value) {
        creativeTabLang.put(key, value);
    }

    public Map<String, String> getCreativeTabLang() {
        return creativeTabLang;
    }

    public Map<String, String> getLangEntries() {
        return langEntries;
    }

    public Map<Identifier, Block> getBlockstateEntries() {
        return blockstateEntries;
    }

    public Map<Identifier, Item> getItemModelEntries() {
        return itemModelEntries;
    }

    public Map<Identifier, Block> getLootEntries() {
        return lootEntries;
    }

    public List<Consumer<RecipeOutput>> getRecipeEntries() {
        return recipeEntries;
    }

    // ------------------------------------------------------------
    // Active creative tab (for auto‑population)
    // ------------------------------------------------------------
    public void setActiveCreativeTab(FERCreativeTabBuilder tab) {
        this.activeTab = tab;
    }

    public FERCreativeTabBuilder getActiveCreativeTab() {
        return this.activeTab;
    }

    public void tryAddToCreativeTab(Supplier<ItemStack> stack) {
        if (this.activeTab != null) {
            this.activeTab.add(stack);
        }
    }

    public boolean hasCreativeTab() {
        return this.activeTab != null;
    }
    public final Map<CreativeModeTab, List<Supplier<ItemStack>>> explicitTabEntries = new HashMap<>();

    public void addToSpecificCreativeTab(CreativeModeTab tab, Supplier<ItemStack> stack) {
        explicitTabEntries.computeIfAbsent(tab, t -> new ArrayList<>()).add(stack);
    }

    //------------------\
    // Datagen          |
    //------------------/
    public void registerLang(String key, String value) {
        langEntries.put(key, value);
    }

    public void registerBlockstate(String name, Block block) {
        blockstateEntries.put(Identifier.fromNamespaceAndPath(modId, name), block);
    }

    public void registerItemModel(String name, Item item) {
        itemModelEntries.put(Identifier.fromNamespaceAndPath(modId, name), item);
    }

    public void registerLoot(String name, Block block) {
        lootEntries.put(Identifier.fromNamespaceAndPath(modId, name), block);
    }

    public void registerRecipe(Consumer<RecipeOutput> builder) {
        recipeEntries.add(builder);
    }

    public static String SnakeToPascalCase(String snake) {
        if (snake == null || snake.isEmpty()) {
            return snake;
        }

        StringBuilder result = new StringBuilder();
        String[] parts = snake.split("_");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }

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

    public void registerBlockTag(TagKey<Block> tag, Block block) {
        blockTagEntries.computeIfAbsent(tag, t -> new java.util.ArrayList<>()).add(block);
    }

    public void registerFluidTag(TagKey<Fluid> tag, Fluid fluid) {
        fluidTagEntries.computeIfAbsent(tag, t -> new java.util.ArrayList<>()).add(fluid);
    }

    public void registerItemTag(TagKey<Item> tag, Item item) {
        itemTagEntries.computeIfAbsent(tag, t -> new java.util.ArrayList<>()).add(item);
    }

    public Map<TagKey<Block>, List<Block>> getBlockTagEntries() {
        return blockTagEntries;
    }

    public Map<TagKey<Item>, List<Item>> getItemTagEntries() {
        return itemTagEntries;
    }

    public void copyBlockTagToItemTag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
        copiedBlockTags.put(blockTag, itemTag);
    }

    public Map<TagKey<Block>, TagKey<Item>> getCopiedBlockTags() {
        return copiedBlockTags;
    }

    // ------------------------------------------------------------
    // ⭐ BUILDER ENTRY POINTS
    // ------------------------------------------------------------
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

    public FERCreativeTabBuilder creativeTab(String name) {
        return new FERCreativeTabBuilder(this, name);
    }

    public <S extends FlowingFluid, F extends FlowingFluid> FERFluidBuilder<S, F> fluid(String name) {
        return new FERFluidBuilder<>(this, name);
    }
}
