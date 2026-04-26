package com.footdablit2310.footlib.api.common.multiblock;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import net.minecraft.world.level.block.Block;

/**
 * Holds all tiered patterns for a single multiblock.
 *
 * tier -> (coords -> block)
 * coords = [x, y, z] relative positions
 */
public class MultiBlockDataFormat {

    private final Map<Integer, Map<List<Integer>, Block>> tierPatterns;

    public MultiBlockDataFormat(Map<Integer, Map<List<Integer>, Block>> tierPatterns) {
        this.tierPatterns = Map.copyOf(tierPatterns);
    }

    /** All tiers defined for this multiblock. */
    public Map<Integer, Map<List<Integer>, Block>> tierPatterns() {
        return tierPatterns;
    }

    /** Pattern for a specific tier, or empty if not present. */
    public Map<List<Integer>, Block> patternForTier(int tier) {
        return tierPatterns.getOrDefault(tier, Collections.emptyMap());
    }

    /** Highest tier defined, or empty if none. */
    public OptionalInt maxTier() {
        return tierPatterns.keySet().stream()
                .mapToInt(Integer::intValue)
                .max();
    }

    /** True if this multiblock has a pattern for the given tier. */
    public boolean hasTier(int tier) {
        return tierPatterns.containsKey(tier);
    }

    /** Total number of tiers defined. */
    public int tierCount() {
        return tierPatterns.size();
    }
}
