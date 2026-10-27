package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FERFluidBuilder<S extends FlowingFluid, F extends FlowingFluid> {

    private final @NotNull FootEasyRegisterSystem reg;
    private final @NotNull String name;

    private DeferredHolder<Item, BucketItem> bucketItem;
    private DeferredHolder<Block, LiquidBlock> fluidBlock;
    private boolean addBucketItem = false;
    private boolean addFluidBlock = false;

    private boolean addToCreativeTab = false;
    private @Nullable ResourceKey<CreativeModeTab> explicitTab = null;
    private Supplier<FluidType> typeSupplier;
    private Supplier<S> sourceSupplier;
    private Supplier<F> flowingSupplier;
    private FERFluidCreativeTabConfig fluidCreativeTabConfig;
    private String tabName;

    public record FERFluidCreativeTabConfig(
            boolean bucketItem,
            boolean fluidBlock
    ) {
        public static @NotNull FERFluidCreativeTabConfig DEFAULT = new FERFluidCreativeTabConfig(true, false);
    }

    public FERFluidBuilder(@NotNull FootEasyRegisterSystem reg, @NotNull String name) {
        this.reg = reg;
        this.name = name;
    }

    public FERFluidBuilder<S, F> creativeTab(@NotNull String tabName, @Nullable FERFluidCreativeTabConfig fluidCreativeTabConfig) {
        this.addToCreativeTab = true;
        if (fluidCreativeTabConfig != null) {
            this.fluidCreativeTabConfig = fluidCreativeTabConfig;
        } else {
            this.fluidCreativeTabConfig = FERFluidCreativeTabConfig.DEFAULT;
            FootLib.LOGGER.warn("No defined Config, using default fallback");
        }
        this.tabName = tabName;
        return this;
    }

    public FERFluidBuilder<S, F> customCreativeTab(ResourceKey<CreativeModeTab> tab, @Nullable FERFluidCreativeTabConfig fluidCreativeTabConfig) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        if (fluidCreativeTabConfig != null) {
            this.fluidCreativeTabConfig = fluidCreativeTabConfig;
        } else {
            this.fluidCreativeTabConfig = FERFluidCreativeTabConfig.DEFAULT;
            FootLib.LOGGER.warn("No defined Config, using default fallback");
        }
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
    /*
    Use this to make a Fluid with only FluidType and BaseFlowingFluid.
    This uses the record FluidProperties to get the properties.
     */
    @SuppressWarnings("unchecked") // The SuppressWarnings is here so that IntelliJ is happy.
    public FERFluidBuilder<S, F> createFromProperties(FluidProperties fluidProperties) {
        this.typeSupplier = () -> new FluidType(fluidProperties.fluidTypeProperties);
        this.sourceSupplier = () -> (S) new BaseFlowingFluid.Source(fluidProperties.sourceProperties); // The cast is here so that IntelliJ is happy.
        this.flowingSupplier = () -> (F) new BaseFlowingFluid.Flowing(fluidProperties.flowingProperties); // The  cast is here so that IntelliJ is happy.
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
                if (fluidCreativeTabConfig.bucketItem)
                    reg.addToExistingTab(explicitTab, () -> new ItemStack(bucketItem.get()));
                if (fluidCreativeTabConfig.fluidBlock)
                    reg.addToExistingTab(explicitTab, () -> new ItemStack(fluidBlock.get()));
            } else {

                if (fluidCreativeTabConfig.bucketItem)
                    reg.addToTab(tabName, () -> new ItemStack(bucketItem.get()));
                if (fluidCreativeTabConfig.fluidBlock)
                    reg.addToTab(tabName, () -> new ItemStack(fluidBlock.get()));
            }
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
    ) {
        public BaseFlowingFluid.Properties createDefaultBaseFlowingFluidProperty() {
            return new BaseFlowingFluid.Properties(
                    this.type(),
                    this.source(),
                    this.flowing()
            );
        }
    }
    public record FluidProperties(
            FluidType.Properties fluidTypeProperties,
            BaseFlowingFluid.Properties sourceProperties,
            BaseFlowingFluid.Properties flowingProperties
    ) {
        /*
        Use this to make a FluidProperties Type without having to duplicate code for source and flowing.
        If you want to use a different Source and Flowing Property then use the record constructor instead.
         */
        public static FluidProperties createPropertySimple(FluidType.Properties fluidTypeProperties, BaseFlowingFluid.Properties sourceAndFlowingProperties) {
            return new FluidProperties(fluidTypeProperties, sourceAndFlowingProperties, sourceAndFlowingProperties);
        }
    }

}
