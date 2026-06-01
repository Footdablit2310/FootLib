package com.footdablit2310.footlib.datagen.models;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.models.BlockModelGenerators;

public final class FERBlockStateHelper {

    private FERBlockStateHelper() {}

    public static void simpleCube(BlockModelGenerators gen, Block block) {
        gen.createTrivialCube(block);
    }
}
