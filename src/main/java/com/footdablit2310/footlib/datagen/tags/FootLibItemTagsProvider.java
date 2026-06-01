package com.footdablit2310.footlib.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class FootLibItemTagsProvider extends ItemTagsProvider {

    protected FootLibItemTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> lookup,
                                  CompletableFuture<TagLookup<Block>> blockTags,
                                  String modid,
                                  ExistingFileHelper existingFileHelper) {
        super(output, lookup, blockTags, modid, existingFileHelper);
    }

    /** Add an item to a tag (1.21 requires ResourceKey<Item>) */
    protected void addTo(TagAppender<Item> app, Item item) {
        app.add(BuiltInRegistries.ITEM.getResourceKey(item)
            .orElseThrow(() -> new IllegalStateException("Unregistered item: " + item)));
    }
}
