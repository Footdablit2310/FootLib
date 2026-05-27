package com.footdablit2310.footlib.generic_base.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.minecraft.world.level.material.FluidState;

public abstract class FootFlowingFluid extends FlowingFluid {

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getAmount(FluidState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public abstract Fluid getFlowing();

    @Override
    public abstract Fluid getSource();

    @Override
    public abstract FluidType getFluidType();
}
