package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.generic_base.fluid.FootFluidType;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FERFluidBuilder<S extends FlowingFluid, F extends FlowingFluid> {

    public record FluidRegistration<S extends FlowingFluid, F extends FlowingFluid>(
        DeferredHolder<FluidType, FluidType> type,
        DeferredHolder<Fluid, S> source,
        DeferredHolder<Fluid, F> flowing,
        BaseFlowingFluid.Properties properties
    ) {}


    private final FootEasyRegisterSystem reg;
    private final String name;

    private Supplier<FluidType> typeFactory;
    private Supplier<S> sourceFactory;
    private Supplier<F> flowingFactory;

    public DeferredHolder<FluidType, FluidType> type;
    public DeferredHolder<Fluid, S> source;
    public DeferredHolder<Fluid, F> flowing;

    // Bucket
    private boolean makeBucket = false;
    private Supplier<Item.Properties> bucketProps = () -> new Item.Properties().stacksTo(1);
    public DeferredHolder<Item, BucketItem> bucket;

    // Fluid block
    private boolean makeFluidBlock = false;
    private Supplier<BlockBehaviour.Properties> blockProps = () -> BlockBehaviour.Properties.of()
            .noCollission()
            .strength(100.0F)
            .noLootTable();
    public DeferredHolder<Block, LiquidBlock> fluidBlock;

    // Creative tab
    private boolean addToCreativeTab = false;
    private CreativeModeTab creativeTab = null;

    // Static creative tab registry
    private static final List<Consumer<net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent>> creativeTabEntries =
            new ArrayList<>();


    public FERFluidBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    // -------------------------------
    // Builder configuration
    // -------------------------------

    public FERFluidBuilder<S, F> type(Supplier<FluidType> typeFactory) {
        this.typeFactory = typeFactory;
        return this;
    }

    public FERFluidBuilder<S, F> source(Supplier<S> sourceFactory) {
        this.sourceFactory = sourceFactory;
        return this;
    }

    public FERFluidBuilder<S, F> flowing(Supplier<F> flowingFactory) {
        this.flowingFactory = flowingFactory;
        return this;
    }

    public FERFluidBuilder<S, F> bucketItem() {
        this.makeBucket = true;
        this.bucketProps = () -> new Item.Properties().stacksTo(1);
        return this;
    }

    public FERFluidBuilder<S, F> bucketItem(Supplier<Item.Properties> props) {
        this.makeBucket = true;
        this.bucketProps = props;
        return this;
    }

    public FERFluidBuilder<S, F> fluidBlock() {
        this.makeFluidBlock = true;
        return this;
    }

    public FERFluidBuilder<S, F> fluidBlock(Supplier<BlockBehaviour.Properties> props) {
        this.makeFluidBlock = true;
        this.blockProps = props;
        return this;
    }

    public FERFluidBuilder<S, F> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.creativeTab = tab;
        return this;
    }

    // -------------------------------
    // Registration
    // -------------------------------

    public FluidRegistration<S, F> register() {

        // 1. FluidType fallback
        if (typeFactory == null) {
            typeFactory = () -> new FootFluidType(FluidType.Properties.create());
        }

        type = reg.fluidTypes.register(name, typeFactory);

        // 2. Source fluid required
        if (sourceFactory == null) {
            throw new IllegalStateException("Fluid '" + name + "' is missing a source fluid class");
        }

        source = reg.fluids.register(name, sourceFactory);

        // 3. Flowing fluid required
        if (flowingFactory == null) {
            throw new IllegalStateException("Fluid '" + name + "' is missing a flowing fluid class");
        }

        flowing = reg.fluids.register("flowing_" + name, flowingFactory);

        // 4. Create BaseFlowingFluid.Properties
        BaseFlowingFluid.Properties props =
                new BaseFlowingFluid.Properties(type, source, flowing);

        // ---------------------------------------------------------
        // 5. Determine creative tab (fallback logic)
        // ---------------------------------------------------------
        CreativeModeTab finalTab = creativeTab;

        if (addToCreativeTab && finalTab == null) {
            FootLib.LOGGER.warn("Fluid '{}' did not specify a creative tab. Using FootLib default tab.", name);
            finalTab = FootLib.FOOTLIB_TAB.get();
        }

        // ---------------------------------------------------------
        // 6. Optional fluid block
        // ---------------------------------------------------------
        if (makeFluidBlock) {
            fluidBlock = reg.blocks.register(name + "_fluid_block", () ->
                new LiquidBlock(source.get(), blockProps.get())
            );

            if (addToCreativeTab && finalTab != null) {
                CreativeModeTab tab = finalTab;
                creativeTabEntries.add(event -> {
                    if (event.getTab() == tab) {
                        event.accept(fluidBlock.get());
                    }
                });
            }
        }

        // ---------------------------------------------------------
        // 7. Optional bucket item
        // ---------------------------------------------------------
        if (makeBucket) {
            bucket = reg.items.register(name + "_bucket", () ->
                new BucketItem(source.get(), bucketProps.get())
            );

            if (addToCreativeTab && finalTab != null) {
                CreativeModeTab tab = finalTab;
                creativeTabEntries.add(event -> {
                    if (event.getTab() == tab) {
                        event.accept(bucket.get());
                    }
                });
            }
        }

        // ---------------------------------------------------------
        // 8. Return the registration object
        // ---------------------------------------------------------
        return new FluidRegistration<S, F>(type, source, flowing, props);
    }
}
