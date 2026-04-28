package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import java.util.Map;
import java.util.HashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record ShapeCoords(Map<ResourceLocation, MultiBlockDefinition> blockdata) {
    public ShapeCoords {
        blockdata = Map.copyOf(blockdata);
    }

    public Map<BlockPos, BlockState> blocks() {
        Map<BlockPos, BlockState> result = new HashMap<>();
        blockdata.values().forEach(def -> def.blocks().forEach(result::put));
        return result;
    }
}
