package com.footdablit2310.footlib.easy_register.helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public final class FERBlockstateModelHelper {

    private FERBlockstateModelHelper() {}

    public static void simpleCube(BlockStateProvider prov, Block block) {
        prov.simpleBlock(block);
    }

    public static void cubeAllWithItem(BlockStateProvider prov, Block block) {
        prov.simpleBlockWithItem(block, prov.cubeAll(block));
    }

    public static void horizontal(BlockStateProvider prov, Block block, ResourceLocation model) {
        ModelFile file = prov.models().getExistingFile(model);
        prov.horizontalBlock(block, file);
    }
}
