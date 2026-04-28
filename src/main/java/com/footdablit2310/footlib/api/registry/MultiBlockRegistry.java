package com.footdablit2310.footlib.api.registry;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.footdablit2310.footlib.api.registry.helpers.multiblock.MultiBlockDefinition;

import java.util.HashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Registry for multiblock definitions.
 * One ID per machine, multiple tiers inside the data.
 */
public final class MultiBlockRegistry {

    private static final Map<ResourceLocation, MultiBlockDefinition> REGISTRY = new HashMap<>();

    public static void clear() {
        REGISTRY.clear();
    }

    public static void register(ResourceLocation id, Map<BlockPos, BlockState> blocks) {
        BlockPos min = computeMin(blocks.keySet());
        BlockPos max = computeMax(blocks.keySet());
        BlockPos size = new BlockPos(
                max.getX() - min.getX() + 1,
                max.getY() - min.getY() + 1,
                max.getZ() - min.getZ() + 1
        );

        MultiBlockDefinition def = new MultiBlockDefinition(id, blocks, size, min, max);
        REGISTRY.put(id, def);
    }

    public static MultiBlockDefinition get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Set<ResourceLocation> allIds() {
        return REGISTRY.keySet();
    }

    private static BlockPos computeMin(Collection<BlockPos> positions) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        for (BlockPos p : positions) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            minZ = Math.min(minZ, p.getZ());
        }
        return new BlockPos(minX, minY, minZ);
    }

    private static BlockPos computeMax(Collection<BlockPos> positions) {
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (BlockPos p : positions) {
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
            maxZ = Math.max(maxZ, p.getZ());
        }
        return new BlockPos(maxX, maxY, maxZ);
    }
}
