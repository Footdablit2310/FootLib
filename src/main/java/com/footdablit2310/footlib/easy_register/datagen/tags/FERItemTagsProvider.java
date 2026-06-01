package com.footdablit2310.footlib.easy_register.datagen.tags;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FERItemTagsProvider extends ItemTagsProvider {

    private final FootEasyRegisterSystem reg;

    // -----------------------------
    // Constructor WITHOUT parent provider
    // -----------------------------
    public FERItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            String modId,
            ExistingFileHelper helper,
            FootEasyRegisterSystem reg
    ) {
        super(output, lookupProvider, blockTags, modId, helper);
        this.reg = reg;
    }

    // -----------------------------
    // Constructor WITH parent provider (optional)
    // -----------------------------
    public FERItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Item>> parentProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            String modId,
            ExistingFileHelper helper,
            FootEasyRegisterSystem reg
    ) {
        super(output, lookupProvider, parentProvider, blockTags, modId, helper);
        this.reg = reg;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        // -----------------------------
        // 1. FER custom item tags
        // -----------------------------
        for (Map.Entry<TagKey<Item>, List<Item>> entry : reg.getItemTagEntries().entrySet()) {
            var tag = entry.getKey();
            var items = entry.getValue();

            var builder = tag(tag);
            for (Item item : items) {
                builder.add(item);
            }
        }

        // -----------------------------
        // 2. Block → Item tag copying (if user configured it)
        // -----------------------------
        for (Map.Entry<TagKey<Block>, TagKey<Item>> entry : reg.getCopiedBlockTags().entrySet()) {
            TagKey<Block> blockTag = entry.getKey();
            TagKey<Item> itemTag = entry.getValue();

            copy(blockTag, itemTag);
        }
    }
}
