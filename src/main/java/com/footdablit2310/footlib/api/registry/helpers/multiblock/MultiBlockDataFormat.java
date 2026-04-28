package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import java.util.Map;

/**
 * Holds all tiered patterns for a single multiblock.
 * tier -> (coords -> block)
 */
public class MultiBlockDataFormat {

    private final Map<Integer, ShapeCoords> tierShapes;

    public MultiBlockDataFormat(Map<Integer, ShapeCoords> tierShapes) {
        this.tierShapes = Map.copyOf(tierShapes);
    }

    public ShapeCoords shapeForTier(int tier) {
        return tierShapes.get(tier);
    }

    public boolean hasTier(int tier) {
        return tierShapes.containsKey(tier);
    }

    public int tierCount() {
        return tierShapes.size();
    }

    public Map<Integer, ShapeCoords> allShapes() {
        return tierShapes;
    }
}
