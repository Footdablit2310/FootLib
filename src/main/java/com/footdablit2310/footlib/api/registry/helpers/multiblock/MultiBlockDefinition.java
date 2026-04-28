package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record MultiBlockDefinition(
    ResourceLocation id,
    Map<BlockPos, BlockState> blocks,
    BlockPos size,          // maxX, maxY, maxZ
    BlockPos min,           // minX, minY, minZ
    BlockPos max            // maxX, maxY, maxZ
) {}
