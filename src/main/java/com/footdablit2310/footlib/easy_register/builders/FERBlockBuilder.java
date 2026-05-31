package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.CreativeModeTab;
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

    private Consumer<RecipeOutput> recipeBuilder = null;

    public FERBlockBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    public FERBlockBuilder<T> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }

    public FERBlockBuilder<T> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    public FERBlockBuilder<T> datagen() {
        this.enableDatagen = true;
        return this;
    }

    public FERBlockBuilder<T> recipe(Consumer<RecipeOutput> builder) {
        this.recipeBuilder = builder;
        return this;
    }

    public DeferredHolder<Block, T> register() {

        DeferredHolder<Block, T> holder = reg.blocks.register(name, factory);

        // CREATIVE TAB
        if (addToCreativeTab) {
            if (explicitTab != null) {
                reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(holder.get()));
            } else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Block '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }
                reg.tryAddToCreativeTab(() -> new ItemStack(holder.get()));
            }
        }

        // DATAGEN
        if (enableDatagen) {
            reg.registerBlockstate(name, holder.get());
            reg.registerItemModel(name, holder.get().asItem());
            reg.registerLoot(name, holder.get());
            reg.registerLang(holder.get().getDescriptionId(),
                FootEasyRegisterSystem.SnakeToPascalCase(name));
        }

        // RECIPE
        if (recipeBuilder != null) {
            reg.registerRecipe(recipeBuilder);
        }

        return holder;
    }
}
