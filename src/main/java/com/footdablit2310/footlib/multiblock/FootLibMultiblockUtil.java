package com.footdablit2310.footlib.multiblock;

import com.footdablit2310.footlib.registry.MultiBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.*;

public final class FootLibMultiblockUtil {

    private FootLibMultiblockUtil() {}

    // ---------------------------
    // Find all multiblocks for a BE type
    // ---------------------------

    public static List<ResourceLocation> findAllIds(BlockEntityType<?> type) {
        List<ResourceLocation> ids = new ArrayList<>();

        for (ResourceLocation id : MultiBlockRegistry.MULTIBLOCK_REGISTRY.keySet()) {
            MultiBlockData data = MultiBlockRegistry.MULTIBLOCK_REGISTRY.get(id);
            if (data.controllerBEType() == type) {
                ids.add(id);
            }
        }

        return ids;
    }

    public static MultiBlockData getById(ResourceLocation id) {
        return MultiBlockRegistry.MULTIBLOCK_REGISTRY.get(id);
    }

    // ---------------------------
    // Fast-path: block count validation
    // ---------------------------

    public static boolean validateCounts(Level level, BlockPos origin, MultiBlockData data) {
        Map<Block, Integer> required = new HashMap<>();
        Map<Block, Integer> world = new HashMap<>();

        for (var entry : data.blockAndPositions().entrySet()) {
            required.put(entry.getKey(), entry.getValue().size());
        }

        for (var entry : data.blockAndPositions().entrySet()) {
            Block block = entry.getKey();
            int count = 0;

            for (BlockPos rel : entry.getValue()) {
                BlockPos target = origin.offset(rel);
                if (level.getBlockState(target).is(block)) {
                    count++;
                }
            }

            world.put(block, count);
        }

        for (var entry : required.entrySet()) {
            if (!Objects.equals(entry.getValue(), world.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    // ---------------------------
    // Slow-path: positional validation
    // ---------------------------

    public static boolean validatePositions(Level level, BlockPos origin, MultiBlockData data) {
        for (var entry : data.blockAndPositions().entrySet()) {
            Block block = entry.getKey();

            for (BlockPos rel : entry.getValue()) {
                BlockPos target = origin.offset(rel);

                if (!level.getBlockState(target).is(block)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ---------------------------
    // Re-validation for ticking BE
    // ---------------------------

    public static boolean isStillValid(Level level, BlockPos origin, MultiBlockData data) {
        return validateCounts(level, origin, data)
                && validatePositions(level, origin, data);
    }
}
