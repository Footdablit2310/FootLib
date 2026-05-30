package com.footdablit2310.footlib.easy_register.datagen.models;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.world.level.block.Block;

public abstract class FERBlockStateProvider extends BlockStateProvider {

    protected final HolderLookup.Provider lookup;

    public FERBlockStateProvider(PackOutput output,
                                 String modid,
                                 CompletableFuture<HolderLookup.Provider> lookup,
                                 ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.lookup = lookup.join();
    }

    /** Simple cube-all block */
    protected void simpleCube(Block block) {
        simpleBlock(block);
    }

    /** Simple block with item model */
    protected void simpleCubeWithItem(Block block) {
        simpleBlock(block);
        simpleBlockItem(block, cubeAll(block));
    }
}
