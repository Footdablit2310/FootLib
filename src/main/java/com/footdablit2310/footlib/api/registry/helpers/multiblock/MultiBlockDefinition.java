package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import java.util.Collection;
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
) {
    
    private static BlockPos computeMin(Collection<BlockPos> positions) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        for (BlockPos p : positions) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            minZ = Math.min(minZ, p.getZ());
        }
        return new BlockPos(minX, minY, minZ);
    }

    private static BlockPos computeMax(Collection<BlockPos> positions) {
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (BlockPos p : positions) {
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
            maxZ = Math.max(maxZ, p.getZ());
        }
        return new BlockPos(maxX, maxY, maxZ);
    }

    public static MultiBlockDefinition CreateMBDType(ResourceLocation id, Map<BlockPos, BlockState> blocks) {
        BlockPos min = computeMin(blocks.keySet());
        BlockPos max = computeMax(blocks.keySet());
        BlockPos size = new BlockPos(
                max.getX() - min.getX() + 1,
                max.getY() - min.getY() + 1,
                max.getZ() - min.getZ() + 1
        );
        return new MultiBlockDefinition(id, Map.copyOf(blocks), size, min, max);
        
    }
}
