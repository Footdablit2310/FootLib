package com.footdablit2310.footlib.api.common.nbt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Unified advanced NBT utilities for BlockEntities in FootLib.
 *
 * This class provides high-level helpers that combine item + fluid
 * serialization into clean, minimal calls for FootOP machines.
 */
public final class BlockEntityNbtAdvUtil {

    private BlockEntityNbtAdvUtil() {}

    // ------------------------------------------------------------
    // Items
    // ------------------------------------------------------------
    public static void writeItems(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        ItemNbtAdvUtil.writeHandler(tag, key, handler, provider);
    }

    public static void readItems(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        ItemNbtAdvUtil.readHandler(tag, key, handler, provider);
    }

    // ------------------------------------------------------------
    // Fluids
    // ------------------------------------------------------------
    public static void writeFluids(CompoundTag tag, String key, FluidTank tank, HolderLookup.Provider provider) {
        FluidNbtAdvUtil.writeTank(tag, key, tank, provider);
    }

    public static void readFluids(CompoundTag tag, String key, FluidTank tank, HolderLookup.Provider provider) {
        FluidNbtAdvUtil.readTank(tag, key, tank, provider);
    }

    // ------------------------------------------------------------
    // Combined (items + fluids)
    // ------------------------------------------------------------
    public static void writeAll(CompoundTag tag,
                                HolderLookup.Provider provider,
                                ItemStackHandler items,
                                FluidTank fluids) {

        ItemNbtAdvUtil.writeHandler(tag, "Items", items, provider);
        FluidNbtAdvUtil.writeTank(tag, "Fluids", fluids, provider);
    }

    public static void readAll(CompoundTag tag,
                               HolderLookup.Provider provider,
                               ItemStackHandler items,
                               FluidTank fluids) {

        ItemNbtAdvUtil.readHandler(tag, "Items", items, provider);
        FluidNbtAdvUtil.readTank(tag, "Fluids", fluids, provider);
    }
}
