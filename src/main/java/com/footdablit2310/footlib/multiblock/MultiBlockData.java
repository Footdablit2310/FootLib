package com.footdablit2310.footlib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.Map;

public record MultiBlockData(
        BlockEntityType<? extends BlockEntity> controllerBEType,
        Map<Block, List<BlockPos>> blockAndPositions
) {
}
