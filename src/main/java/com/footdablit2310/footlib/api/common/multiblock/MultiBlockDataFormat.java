package com.footdablit2310.footlib.api.common.multiblock;

import java.util.Map;

/**
 * Holds all tiered patterns for a single multiblock.
 *
 * tier -> (coords -> block)
 * coords = [x, y, z] relative positions
 */
public class MultiBlockDataFormat {

    private final Map<Integer, MultiBlockShape> tierShapes;

    public MultiBlockDataFormat(Map<Integer, MultiBlockShape> tierShapes) {
        this.tierShapes = Map.copyOf(tierShapes);
    }

    public MultiBlockShape shapeForTier(int tier) {
        return tierShapes.get(tier);
    }

    public boolean hasTier(int tier) {
        return tierShapes.containsKey(tier);
    }

    public int tierCount() {
        return tierShapes.size();
    }

    public Map<Integer, MultiBlockShape> allShapes() {
        return tierShapes;
    }
}
