package com.footdablit2310.footlib.api.integration.create;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FootLibCreateHeatBridge {

    public static BlazeBurnerBlock.HeatLevel map(FootLibHeatLevel level) {
        return switch (level) {
            case SUPERHEATED -> BlazeBurnerBlock.HeatLevel.SEETHING;
            case HEATED -> BlazeBurnerBlock.HeatLevel.KINDLED;
            default -> BlazeBurnerBlock.HeatLevel.NONE;
        };
    }

    public static BlazeBurnerBlock.HeatLevel getHeatFor(BlockEntity be) {
        if (be instanceof FootLibHeatProvider provider)
            return map(provider.footlib$getHeatLevel());
        return BlazeBurnerBlock.HeatLevel.NONE;
    }
}
