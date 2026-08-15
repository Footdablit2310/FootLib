package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FERBlockBuilder<T extends Block> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> factory;

    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;
    private boolean enableDatagen = false;
    private boolean createSimpleItem = false;

    private Consumer<RecipeOutput> recipeBuilder = null;
    
    private final java.util.List<net.minecraft.tags.TagKey<Block>> blockTags = new java.util.ArrayList<>();
    private final java.util.List<net.minecraft.tags.TagKey<net.minecraft.world.item.Item>> itemTags = new java.util.ArrayList<>();


    public FERBlockBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    //TAGS
    public FERBlockBuilder<T> blockTag(TagKey<Block> tag) {
        this.blockTags.add(tag);
        return this;
    }


    public FERBlockBuilder<T> itemTag(TagKey<Item> tag) {
        this.itemTags.add(tag);
        return this;
    }

    //CREATIVE TAB
    public FERBlockBuilder<T> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }

    public FERBlockBuilder<T> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    //DATAGEN
    public FERBlockBuilder<T> datagen() {
        this.enableDatagen = true;
        return this;
    }

    //RECIPE
    public FERBlockBuilder<T> recipe(Consumer<RecipeOutput> builder) {
        this.recipeBuilder = builder;
        return this;
    }

    //SIMPLE ITEM
    public FERBlockBuilder<T> simpleItem() {
        // Register a BlockItem for this block
        this.createSimpleItem = true;
        return this;
    }

    //REGISTER
    public DeferredHolder<Block, T> register() {

        DeferredHolder<Block, T> holder = reg.blocks.register(name, factory);

        // --- SIMPLE ITEM ---
        if (createSimpleItem) {
            reg.items.register(name, () ->
                new BlockItem(holder.get(), new net.minecraft.world.item.Item.Properties())
            );
        }

        // --- CREATIVE TAB ---
        if (addToCreativeTab) {
            Supplier<ItemStack> stackSupplier = () ->
                new ItemStack(createSimpleItem ? holder.get().asItem() : holder.get());

            if (explicitTab != null) {
                reg.addToSpecificCreativeTab(explicitTab, stackSupplier);
            } else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Block '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }
                reg.tryAddToCreativeTab(stackSupplier);
            }
        }

        // --- DATAGEN ---
        if (enableDatagen) {
            reg.registerBlockstate(name, holder.get());
            reg.registerLoot(name, holder.get());
            reg.registerLang(holder.get().getDescriptionId(),
                FootEasyRegisterSystem.SnakeToNormalCase(name));

            // Item model only if item exists
            if (createSimpleItem) {
                reg.registerItemModel(name, holder.get().asItem());
            }

            // TAGS
            for (var tag : blockTags) {
                reg.registerBlockTag(tag, holder.get());
            }
            if (createSimpleItem) {
                for (var tag : itemTags) {
                    reg.registerItemTag(tag, holder.get().asItem());
                }
            }
        }

        // --- RECIPE ---
        if (recipeBuilder != null) {
            reg.registerRecipe(recipeBuilder);
        }

        return holder;
    }


}
