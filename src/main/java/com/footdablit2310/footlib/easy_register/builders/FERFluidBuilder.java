package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FERFluidBuilder<S extends FlowingFluid, F extends FlowingFluid> {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private DeferredHolder<Item, BucketItem> bucketItem;
    private DeferredHolder<Block, LiquidBlock> fluidBlock;
    private boolean addBucketItem = false;
    private boolean addFluidBlock = false;

    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;
    private boolean enableDatagen = false;

    private Consumer<RecipeOutput> recipeBuilder = null;
    private Supplier<FluidType> typeSupplier;
    private Supplier<S> sourceSupplier;
    private Supplier<F> flowingSupplier;

    private final java.util.List<TagKey<Fluid>> fluidTags = new java.util.ArrayList<>();
    private String customLangName = null;


    public FERFluidBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    public FERFluidBuilder<S, F> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }

    public FERFluidBuilder<S, F> customCreativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    public FERFluidBuilder<S, F> datagen() {
        this.enableDatagen = true;
        return this;
    }

    public FERFluidBuilder<S, F> type(Supplier<FluidType> supplier) {
        this.typeSupplier = supplier;
        return this;
    }

    public FERFluidBuilder<S, F> source(Supplier<S> supplier) {
        this.sourceSupplier = supplier;
        return this;
    }

    public FERFluidBuilder<S, F> flowing(Supplier<F> supplier) {
        this.flowingSupplier = supplier;
        return this;
    }
    @SuppressWarnings("unchecked")
    public FERFluidBuilder<S, F> createFromProperties(FluidProperties fluidProperties) {
        this.typeSupplier = () -> new FluidType(fluidProperties.fluidTypeProperties);
        this.sourceSupplier = () -> (S) new BaseFlowingFluid.Source(fluidProperties.sourceProperties);
        this.flowingSupplier = () -> (F) new BaseFlowingFluid.Flowing(fluidProperties.flowingProperties);
        return this;
    }


    public FERFluidBuilder<S, F> recipe(Consumer<RecipeOutput> builder) {
        this.recipeBuilder = builder;
        return this;
    }

    public FERFluidBuilder<S, F> customBucketItem(DeferredHolder<Item, BucketItem> bucket) {
        this.bucketItem = bucket;
        return this;
    }
    public FERFluidBuilder<S, F> bucketItem() {
        this.addBucketItem=true;
        return this;
    }

    public FERFluidBuilder<S, F> tag(TagKey<Fluid> tag) {
        this.fluidTags.add(tag);
        return this;
    }

    public FERFluidBuilder<S, F> lang(String name) {
        this.customLangName = name;
        return this;
    }

    public FERFluidBuilder<S, F> setCustomFluidBlock(DeferredHolder<Block, LiquidBlock> block) {
        this.fluidBlock = block;
        return this;
    }
    public FERFluidBuilder<S, F> setFluidBlock() {
        this.addFluidBlock=true;
        return this;
    }
    private void finish() {

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
    public FluidRegistration<S, F> register() {

        // 1. Register FluidType
        DeferredHolder<FluidType, FluidType> typeReg =
            reg.fluidTypes.register(name, typeSupplier);

        // 2. Register Source Fluid
        DeferredHolder<Fluid, S> sourceReg =
            reg.fluids.register(name, sourceSupplier);

        DeferredHolder<Fluid, F> flowingReg =
            reg.fluids.register(name + "_flowing", flowingSupplier);

        // FLUID TAGS
        if (!fluidTags.isEmpty()) {
            // Apply fluid tags
            for (TagKey<Fluid> tag : fluidTags) {
                reg.registerFluidTag(tag, sourceReg.get());
            }
        }

        // LANG OVERRIDE
        if (customLangName != null) {
            reg.registerLang("fluid." + reg.getModId() + "." + name, customLangName);
        }

        // 4. Register Bucket Item (optional)
        if (addBucketItem) {
            if (bucketItem == null) {
                bucketItem = reg.items.register(name + "_bucket",
                        () -> new net.minecraft.world.item.BucketItem(sourceReg.get(), new Item.Properties()
                                .craftRemainder(Items.BUCKET)
                                .stacksTo(1).rarity(sourceReg.get().getFluidType().getRarity()))
                );
            }
        }

        // 5. Register Fluid Block (optional)
        if (addFluidBlock) {
            if (fluidBlock == null) {
                fluidBlock = reg.blocks.register(name + "_fluid_block",
                        () -> new LiquidBlock(sourceReg.get(), Block.Properties.of().noLootTable().liquid()));
            }
        }

        // 6. Finish creative tab, datagen, recipes
        this.finish();

        // 7. Return wrapped registration object
        return new FluidRegistration<>(sourceReg, flowingReg, bucketItem, fluidBlock, typeReg);
    }

    public record FluidRegistration<S extends FlowingFluid, F extends FlowingFluid>(
        DeferredHolder<Fluid, S> source,
        DeferredHolder<Fluid, F> flowing,
        DeferredHolder<Item, BucketItem> bucket,
        DeferredHolder<Block, LiquidBlock> block,
        DeferredHolder<FluidType, FluidType> type
    ) {}
    public record FluidProperties(
            FluidType.Properties fluidTypeProperties,
            BaseFlowingFluid.Properties sourceProperties,
            BaseFlowingFluid.Properties flowingProperties
    ) {
        /*
        Use this to make a FluidProperties Type without having to duplicate code for source and flowing.
        If you want to use a different Source and Flowing Property then use the record constructor instead.
         */
        public static FluidProperties createPropertySimple(BaseFlowingFluid.Properties sourceAndFlowingProperties, FluidType.Properties fluidTypeProperties) {
            return new FluidProperties(fluidTypeProperties, sourceAndFlowingProperties, sourceAndFlowingProperties);
        }
    }

}
