package com.footdablit2310.footlib.api.common.multiblock;

import com.footdablit2310.footlib.registry.helpers.multiblock.MultiblockChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class MultiblockCheckerAPI {

    public record Result(boolean valid, String message) {}

    private MultiblockCheckerAPI() {}

    public static Result check(Level level, BlockPos origin, Map<BlockPos, BlockState> blocks) {
        return MultiblockChecker.check(level, origin, blocks);
    }
}
