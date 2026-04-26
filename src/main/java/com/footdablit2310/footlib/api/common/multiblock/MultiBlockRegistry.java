package com.footdablit2310.footlib.api.common.multiblock;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.world.level.block.Block;

/**
 * Registry for multiblock definitions.
 * One ID per machine, multiple tiers inside the data.
 */
public class MultiBlockRegistry {

    public record MultiBlockKey(String id) {}

    private static final Map<MultiBlockKey, MultiBlockDataFormat> MULTI_BLOCKS =
            new LinkedHashMap<>();

    /**
     * Register a multiblock with all its tier patterns.
     *
     * @param id            unique ID for the machine (e.g. "ptf")
     * @param tierPatterns  tier -> (coords -> block)
     */
    public static MultiBlockDataFormat register(
            String id,
            Map<Integer, Map<List<Integer>, Block>> tierPatterns
    ) {
        MultiBlockKey key = new MultiBlockKey(id);
        MultiBlockDataFormat data = new MultiBlockDataFormat(tierPatterns);
        MULTI_BLOCKS.put(key, data);
        return data;
    }

    /** Get the multiblock definition for a given ID, or null if missing. */
    public static MultiBlockDataFormat get(String id) {
        return MULTI_BLOCKS.get(new MultiBlockKey(id));
    }

    /** Get the multiblock definition for a given ID, wrapped in Optional. */
    public static Optional<MultiBlockDataFormat> getOptional(String id) {
        return Optional.ofNullable(get(id));
    }

    /** All registered multiblocks (unmodifiable view). */
    public static Map<MultiBlockKey, MultiBlockDataFormat> getAll() {
        return Collections.unmodifiableMap(MULTI_BLOCKS);
    }

    /** Clear all registered multiblocks (e.g. on datapack reload). */
    public static void clear() {
        MULTI_BLOCKS.clear();
    }

    /** Check if a multiblock with this ID exists. */
    public static boolean contains(String id) {
        return MULTI_BLOCKS.containsKey(new MultiBlockKey(id));
    }

    /** Remove a multiblock by ID. */
    public static void remove(String id) {
        MULTI_BLOCKS.remove(new MultiBlockKey(id));
    }

    /** Total number of registered multiblock machines. */
    public static int count() {
        return MULTI_BLOCKS.size();
    }
}
