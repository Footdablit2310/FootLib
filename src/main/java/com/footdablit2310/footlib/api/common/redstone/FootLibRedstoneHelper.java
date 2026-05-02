package com.footdablit2310.footlib.api.common.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FootLibRedstoneHelper {

    public static boolean isPowered(Level level, BlockPos pos) {
        return level.hasNeighborSignal(pos);
    }

    public static boolean isStrongPowered(Level level, BlockPos pos) {
        return level.getBestNeighborSignal(pos) > 0;
    }
}
