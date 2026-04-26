package com.footdablit2310.footlib.api.common.multiblock;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry for multiblock definitions.
 * One ID per machine, multiple tiers inside the data.
 */
public class MultiBlockRegistry {

    public record MultiBlockKey(String id) {}

    private static final Map<MultiBlockKey, MultiBlockDataFormat> MULTI_BLOCKS =
            new LinkedHashMap<>();

    public static MultiBlockDataFormat register(
            String id,
            Map<Integer, MultiBlockShape> tierShapes
    ) {
        MultiBlockKey key = new MultiBlockKey(id);
        MultiBlockDataFormat data = new MultiBlockDataFormat(tierShapes);
        MULTI_BLOCKS.put(key, data);
        return data;
    }

    public static MultiBlockDataFormat get(String id) {
        return MULTI_BLOCKS.get(new MultiBlockKey(id));
    }

    public static boolean contains(String id) {
        return MULTI_BLOCKS.containsKey(new MultiBlockKey(id));
    }

    public static void remove(String id) {
        MULTI_BLOCKS.remove(new MultiBlockKey(id));
    }

    public static void clear() {
        MULTI_BLOCKS.clear();
    }

    public static int count() {
        return MULTI_BLOCKS.size();
    }
}
