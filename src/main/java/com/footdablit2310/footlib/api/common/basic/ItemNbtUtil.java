package com.footdablit2310.footlib.api.common.basic;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class ItemNbtUtil {

    private ItemNbtUtil() {}

    // ------------------------------------------------------------
    // Write a REQUIRED item (empty stack still saved)
    // ------------------------------------------------------------
    public static void writeItem(CompoundTag tag, String key, ItemStack stack, HolderLookup.Provider provider) {
        tag.put(key, stack.save(provider));
    }

    // ------------------------------------------------------------
    // Read a REQUIRED item (returns EMPTY if missing)
    // ------------------------------------------------------------
    public static ItemStack readItem(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return ItemStack.EMPTY;
        return ItemStack.parseOptional(provider, tag.getCompound(key));
    }

    // ------------------------------------------------------------
    // Write an OPTIONAL item (only save if not empty)
    // ------------------------------------------------------------
    public static void writeOptionalItem(CompoundTag tag, String key, ItemStack stack, HolderLookup.Provider provider) {
        if (!stack.isEmpty()) {
            tag.put(key, stack.save(provider));
        }
    }

    // ------------------------------------------------------------
    // Read an OPTIONAL item (returns EMPTY if missing)
    // ------------------------------------------------------------
    public static ItemStack readOptionalItem(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return ItemStack.EMPTY;
        return ItemStack.parseOptional(provider, tag.getCompound(key));
    }
}
