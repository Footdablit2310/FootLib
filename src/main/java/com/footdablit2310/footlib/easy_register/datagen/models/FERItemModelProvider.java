package com.footdablit2310.footlib.easy_register.datagen.models;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.world.item.Item;

public abstract class FERItemModelProvider extends ItemModelProvider {

    protected final HolderLookup.Provider lookup;

    public FERItemModelProvider(PackOutput output,
                                String modid,
                                CompletableFuture<HolderLookup.Provider> lookup,
                                ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.lookup = lookup.join();
    }

    /** Generated (flat) item */
    protected void generated(Item item) {
        basicItem(item);
    }

    /** Handheld item (tools, weapons) */
    protected void handheld(Item item) {
        handheldItem(item);
    }
}
