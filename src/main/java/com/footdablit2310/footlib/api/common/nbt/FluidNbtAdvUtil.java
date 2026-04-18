package com.footdablit2310.footlib.api.common.nbt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Advanced provider-aware Fluid NBT utilities for FootLib.
 *
 * These methods wrap NeoForge 1.21.1's registry-aware serialization API,
 * providing clean, high-performance helpers for FootOP machines and other mods.
 */
public final class FluidNbtAdvUtil {

    private FluidNbtAdvUtil() {}

    // ------------------------------------------------------------
    // FluidStack (required)
    // ------------------------------------------------------------
    public static void writeFluid(CompoundTag tag, String key, FluidStack stack, HolderLookup.Provider provider) {
        CompoundTag fluidTag = new CompoundTag();
        stack.save(provider, fluidTag);
        tag.put(key, fluidTag);
    }

    public static FluidStack readFluid(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return FluidStack.EMPTY;
        return FluidStack.parseOptional(provider, tag.getCompound(key));
    }

    // ------------------------------------------------------------
    // Optional FluidStack
    // ------------------------------------------------------------
    public static void writeOptionalFluid(CompoundTag tag, String key, FluidStack stack, HolderLookup.Provider provider) {
        if (!stack.isEmpty()) {
            writeFluid(tag, key, stack, provider);
        }
    }

    public static FluidStack readOptionalFluid(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return FluidStack.EMPTY;
        return readFluid(tag, key, provider);
    }

    // ------------------------------------------------------------
    // FluidTank (required)
    // ------------------------------------------------------------
    public static void writeTank(CompoundTag tag, String key, FluidTank tank, HolderLookup.Provider provider) {
        CompoundTag tankTag = new CompoundTag();
        tank.writeToNBT(provider, tankTag);
        tag.put(key, tankTag);
    }

    public static void readTank(CompoundTag tag, String key, FluidTank tank, HolderLookup.Provider provider) {
        if (tag.contains(key)) {
            tank.readFromNBT(provider, tag.getCompound(key));
        }
    }

    // ------------------------------------------------------------
    // Multiple tanks
    // ------------------------------------------------------------
    public static void writeTanks(CompoundTag tag, String key, FluidTank[] tanks, HolderLookup.Provider provider) {
        CompoundTag tanksTag = new CompoundTag();
        for (int i = 0; i < tanks.length; i++) {
            CompoundTag tankTag = new CompoundTag();
            tanks[i].writeToNBT(provider, tankTag);
            tanksTag.put("Tank" + i, tankTag);
        }
        tag.put(key, tanksTag);
    }

    public static void readTanks(CompoundTag tag, String key, FluidTank[] tanks, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return;

        CompoundTag tanksTag = tag.getCompound(key);
        for (int i = 0; i < tanks.length; i++) {
            String tankKey = "Tank" + i;
            if (tanksTag.contains(tankKey)) {
                tanks[i].readFromNBT(provider, tanksTag.getCompound(tankKey));
            }
        }
    }
}
