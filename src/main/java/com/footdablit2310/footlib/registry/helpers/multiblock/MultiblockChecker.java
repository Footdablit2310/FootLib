package com.footdablit2310.footlib.registry.helpers.multiblock;

import com.footdablit2310.footlib.api.common.multiblock.MultiblockCheckerAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class MultiblockChecker {

    private MultiblockChecker() {}

    public static MultiblockCheckerAPI.Result check(
            Level level,
            BlockPos origin,
            Map<BlockPos, BlockState> blocks
    ) {
        for (var entry : blocks.entrySet()) {
            BlockPos rel = entry.getKey();
            BlockState expected = entry.getValue();

            BlockPos worldPos = origin.offset(rel);
            BlockState actual = level.getBlockState(worldPos);

            if (!actual.is(expected.getBlock())) {
                return new MultiblockCheckerAPI.Result(false, "Mismatch at " + worldPos);
            }
        }

        return new MultiblockCheckerAPI.Result(true, "OK");
    }
}
