package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Consumer;

public class FERFluidBuilder<S extends FlowingFluid, F extends FlowingFluid> {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private DeferredHolder<Item, BucketItem> bucketItem;
    private DeferredHolder<Block, LiquidBlock> fluidBlock;

    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;
    private boolean enableDatagen = false;

    private Consumer<RecipeOutput> recipeBuilder = null;

    public FERFluidBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    public FERFluidBuilder<S, F> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }

    public FERFluidBuilder<S, F> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    public FERFluidBuilder<S, F> datagen() {
        this.enableDatagen = true;
        return this;
    }

    public FERFluidBuilder<S, F> recipe(Consumer<RecipeOutput> builder) {
        this.recipeBuilder = builder;
        return this;
    }

    public void setBucket(DeferredHolder<Item, BucketItem> bucket) {
        this.bucketItem = bucket;
    }

    public void setFluidBlock(DeferredHolder<Block, LiquidBlock> block) {
        this.fluidBlock = block;
    }

    public void finish() {

        // CREATIVE TAB
        if (addToCreativeTab) {

            if (explicitTab != null) {
                if (bucketItem != null)
                    reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(bucketItem.get()));
                if (fluidBlock != null)
                    reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(fluidBlock.get()));
            } else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Fluid '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }

                if (bucketItem != null)
                    reg.tryAddToCreativeTab(() -> new ItemStack(bucketItem.get()));
                if (fluidBlock != null)
                    reg.tryAddToCreativeTab(() -> new ItemStack(fluidBlock.get()));
            }
        }

        // DATAGEN
        if (enableDatagen) {
            if (bucketItem != null)
                reg.registerItemModel(name + "_bucket", bucketItem.get());

            reg.registerLang("fluid." + reg.getModId() + "." + name,
                FootEasyRegisterSystem.SnakeToPascalCase(name));
        }

        // RECIPE
        if (recipeBuilder != null) {
            reg.registerRecipe(recipeBuilder);
        }
    }
}
