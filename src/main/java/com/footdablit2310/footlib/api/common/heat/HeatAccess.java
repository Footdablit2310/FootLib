package com.footdablit2310.footlib.api.common.heat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class HeatAccess {

    public static int getTemperature(Level level, BlockPos pos) {
        // TODO: real heat grid
        return 0;
    }

    public static HeatTier getHeatTier(Level level, BlockPos pos) {
        return HeatTier.fromTemperature(getTemperature(level, pos));
    }

    public static HeatInfo getHeatInfo(Level level, BlockPos pos) {
        int temp = getTemperature(level, pos);
        HeatTier tier = HeatTier.fromTemperature(temp);
        return HeatInfo.of(tier, temp);
    }
}
