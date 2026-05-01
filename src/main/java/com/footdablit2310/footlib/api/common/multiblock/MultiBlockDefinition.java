package com.footdablit2310.footlib.api.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public record MultiBlockDefinition(
        Map<BlockPos, BlockState> blocks
) {}
