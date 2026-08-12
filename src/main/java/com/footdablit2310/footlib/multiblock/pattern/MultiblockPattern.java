package com.footdablit2310.footlib.multiblock.pattern;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.TagKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MultiblockPattern {
    private final Map<BlockPos, Predicate<BlockState>> rules = new HashMap<>();

    public MultiblockPattern expect(int x, int y, int z, Block block) {
        rules.put(new BlockPos(x, y, z), state -> state.is(block));
        return this;
    }

    public MultiblockPattern expect(int x, int y, int z, TagKey<Block> tag) {
        rules.put(new BlockPos(x, y, z), state -> state.is(tag));
        return this;
    }

    public MultiblockPattern expect(int x, int y, int z, Predicate<BlockState> predicate) {
        rules.put(new BlockPos(x, y, z), predicate);
        return this;
    }

    public MultiblockPattern air(int x, int y, int z) {
        rules.put(new BlockPos(x, y, z), BlockState::isAir);
        return this;
    }

    public MultiblockPattern any(int x, int y, int z) {
        rules.put(new BlockPos(x, y, z), s -> true);
        return this;
    }

    /** Validate this pattern against the world at the given origin. */
    public PatternResult validate(Level level, BlockPos origin) {
        Map<BlockPos, BlockState> matched = new HashMap<>();
        Map<BlockPos, BlockState> mismatched = new HashMap<>();

        for (var entry : rules.entrySet()) {
            BlockPos check = origin.offset(entry.getKey());
            BlockState state = level.getBlockState(check);
            if (entry.getValue().test(state)) {
                matched.put(check, state);
            } else {
                mismatched.put(check, state);
            }
        }

        return new PatternResult(matched, mismatched);
    }

    public Map<BlockPos, Predicate<BlockState>> getRules() {
        return rules;
    }

    public record PatternResult(
            Map<BlockPos, BlockState> matched,
            Map<BlockPos, BlockState> mismatched
    ) {
        public boolean isValid() {
            return mismatched.isEmpty();
        }

        public int totalBlocks() {
            return matched.size() + mismatched.size();
        }
    }
}