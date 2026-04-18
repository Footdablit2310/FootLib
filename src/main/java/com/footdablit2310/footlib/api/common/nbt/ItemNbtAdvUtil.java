package com.footdablit2310.footlib.api.common.nbt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Advanced provider-aware Item NBT utilities for FootLib.
 * 
 * These methods wrap NeoForge 1.21.1's registry-aware serialization API,
 * providing clean, high-performance helpers for FootOP machines and other mods.
 */
public final class ItemNbtAdvUtil {

    private ItemNbtAdvUtil() {}

    // ------------------------------------------------------------
    // ItemStack (required)
    // ------------------------------------------------------------
    public static void writeItem(CompoundTag tag, String key, ItemStack stack, HolderLookup.Provider provider) {
        CompoundTag itemTag = new CompoundTag();
        stack.save(provider, itemTag);
        tag.put(key, itemTag);
    }

    public static ItemStack readItem(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return ItemStack.EMPTY;
        return ItemStack.parseOptional(provider, tag.getCompound(key));
    }

    // ------------------------------------------------------------
    // Optional ItemStack
    // ------------------------------------------------------------
    public static void writeOptionalItem(CompoundTag tag, String key, ItemStack stack, HolderLookup.Provider provider) {
        if (!stack.isEmpty()) {
            writeItem(tag, key, stack, provider);
        }
    }

    public static ItemStack readOptionalItem(CompoundTag tag, String key, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return ItemStack.EMPTY;
        return readItem(tag, key, provider);
    }

    // ------------------------------------------------------------
    // ItemStackHandler (required)
    // ------------------------------------------------------------
    public static void writeHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        CompoundTag handlerTag = handler.serializeNBT(provider);
        tag.put(key, handlerTag);
    }

    public static void readHandler(CompoundTag tag, String key, ItemStackHandler handler, HolderLookup.Provider provider) {
        if (tag.contains(key)) {
            handler.deserializeNBT(provider, tag.getCompound(key));
        }
    }

    // ------------------------------------------------------------
    // Multiple handlers
    // ------------------------------------------------------------
    public static void writeHandlers(CompoundTag tag, String key, ItemStackHandler[] handlers, HolderLookup.Provider provider) {
        CompoundTag handlersTag = new CompoundTag();
        for (int i = 0; i < handlers.length; i++) {
            handlersTag.put("Handler" + i, handlers[i].serializeNBT(provider));
        }
        tag.put(key, handlersTag);
    }

    public static void readHandlers(CompoundTag tag, String key, ItemStackHandler[] handlers, HolderLookup.Provider provider) {
        if (!tag.contains(key)) return;

        CompoundTag handlersTag = tag.getCompound(key);
        for (int i = 0; i < handlers.length; i++) {
            String handlerKey = "Handler" + i;
            if (handlersTag.contains(handlerKey)) {
                handlers[i].deserializeNBT(provider, handlersTag.getCompound(handlerKey));
            }
        }
    }
}
