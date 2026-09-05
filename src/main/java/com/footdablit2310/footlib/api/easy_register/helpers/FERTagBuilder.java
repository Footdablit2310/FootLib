package com.footdablit2310.footlib.api.easy_register.helpers;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

public final class FERTagBuilder {

    private FERTagBuilder() {}

    public static void addBlocks(TagsProvider.TagAppender<Block> appender, Block... blocks) {
        for (Block b : blocks) {
            ResourceKey<Block> key = BuiltInRegistries.BLOCK.getResourceKey(b)
                .orElseThrow();
            appender.add(key);
        }
    }

}
