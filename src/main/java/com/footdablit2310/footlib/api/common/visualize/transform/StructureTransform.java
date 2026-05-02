package com.footdablit2310.footlib.api.common.visualize.transform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public final class StructureTransform {

    public static BlockPos rotate(BlockPos pos, Direction dir) {
        return switch (dir) {
            case NORTH -> pos;
            case EAST  -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            case SOUTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
            case WEST  -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
            default    -> pos;
        };
    }
}
