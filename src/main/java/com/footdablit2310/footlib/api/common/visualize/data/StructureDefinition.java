package com.footdablit2310.footlib.api.common.visualize.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class StructureDefinition {

    private final Map<BlockPos, Block> layout = new HashMap<>();

    public void add(BlockPos pos, Block block) {
        layout.put(pos, block);
    }

    public Map<BlockPos, Block> getLayout() {
        return layout;
    }
}
