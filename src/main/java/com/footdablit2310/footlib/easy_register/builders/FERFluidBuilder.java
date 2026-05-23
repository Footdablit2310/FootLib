package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public final class FERFluidBuilder {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private Supplier<FluidType> typeFactory;
    private Supplier<Fluid> sourceFactory;
    private Supplier<Fluid> flowingFactory;

    public DeferredHolder<FluidType, FluidType> type;
    public DeferredHolder<Fluid, Fluid> source;
    public DeferredHolder<Fluid, Fluid> flowing;

    public FERFluidBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    public FERFluidBuilder type(Supplier<FluidType> typeFactory) {
        this.typeFactory = typeFactory;
        return this;
    }

    public FERFluidBuilder source(Supplier<Fluid> sourceFactory) {
        this.sourceFactory = sourceFactory;
        return this;
    }

    public FERFluidBuilder flowing(Supplier<Fluid> flowingFactory) {
        this.flowingFactory = flowingFactory;
        return this;
    }

    public void register() {
        if (typeFactory != null) {
            type = reg.fluidTypes.register(name, typeFactory);
        }
        if (sourceFactory != null) {
            source = reg.fluids.register(name, sourceFactory);
        }
        if (flowingFactory != null) {
            flowing = reg.fluids.register("flowing_" + name, flowingFactory);
        }
    }
}
