package com.footdablit2310.footlib.easy_register.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class FERBlockTagsProvider extends BlockTagsProvider {

    protected FERBlockTagsProvider(PackOutput output,
                                   CompletableFuture<HolderLookup.Provider> lookup,
                                   String modid,
                                   ExistingFileHelper existingFileHelper) {
        super(output, lookup, modid, existingFileHelper);
    }

    /** Add a block to a tag (1.21 requires ResourceKey<Block>) */
    protected void addTo(TagAppender<Block> app, Block block) {
        app.add(BuiltInRegistries.BLOCK.getResourceKey(block)
            .orElseThrow(() -> new IllegalStateException("Unregistered block: " + block)));
    }
}
