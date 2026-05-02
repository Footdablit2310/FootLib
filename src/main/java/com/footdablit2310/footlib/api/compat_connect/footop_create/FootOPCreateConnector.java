package com.footdablit2310.footlib.api.compat_connect.footop_create;

import com.footdablit2310.footlib.api.integration.create.FootLibCreateHeatBridge;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FootOPCreateConnector {

    public static BlazeBurnerBlock.HeatLevel getHeat(BlockEntity be) {
        return FootLibCreateHeatBridge.getHeatFor(be);
    }
}
