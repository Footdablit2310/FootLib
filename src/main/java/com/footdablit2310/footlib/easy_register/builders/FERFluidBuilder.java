package com.footdablit2310.footlib.easy_register.builders;

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
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

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
    private CreativeModeTab explicitTab = null;

    // Datagen
    private boolean enableDatagen = false;


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
        this.explicitTab = tab;
        return this;
    }
    public FERFluidBuilder<S, F> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }
    public FERFluidBuilder<S, F> datagen() {
        this.enableDatagen = true;
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
        // 5. Optional fluid block
        // ---------------------------------------------------------
        if (makeFluidBlock) {
            fluidBlock = reg.blocks.register(name + "_fluid_block", () ->
                new LiquidBlock(source.get(), blockProps.get())
            );
        }

        // ---------------------------------------------------------
        // 6. Optional bucket item
        // ---------------------------------------------------------
        if (makeBucket) {
            bucket = reg.items.register(name + "_bucket", () ->
                new BucketItem(source.get(), bucketProps.get())
            );
        }
        // ---------------------------------------------------------
        // 7. Determine creative tab (fallback logic)
        // ---------------------------------------------------------
        if (addToCreativeTab) {
            if (explicitTab != null) {
                if (bucket != null)
                    reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(bucket.get()));
                if (fluidBlock != null)
                    reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(fluidBlock.get()));
            }
            else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Fluid '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }

                if (bucket != null)
                    reg.tryAddToCreativeTab(() -> new ItemStack(bucket.get()));
                if (fluidBlock != null)
                    reg.tryAddToCreativeTab(() -> new ItemStack(fluidBlock.get()));
            }
        }
        if (enableDatagen) {
            if (bucket != null)
                reg.registerItemModel(name + "_bucket", bucket.get());

            reg.registerLang("fluid." + reg.getModId() + "." + name, FootEasyRegisterSystem.SnakeToPascalCase(name));
        }


        // ---------------------------------------------------------
        // 8. Return the registration object
        // ---------------------------------------------------------
        return new FluidRegistration<S, F>(type, source, flowing, props);
    }
}
