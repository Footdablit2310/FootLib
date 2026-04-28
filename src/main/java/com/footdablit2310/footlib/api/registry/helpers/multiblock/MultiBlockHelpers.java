package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

/**
 * Universal helpers for ANY multiblock shape:
 * - Shape2D
 * - Shape3D
 * - ShapeCoords
 *
 * Supports:
 * - Conversion to coordinate map
 * - Rotation (90/180/270)
 * - Mirroring (X/Z)
 * - Offsetting
 * - World matching
 */
public class MultiBlockHelpers {

    // ------------------------------------------------------------
    // Offsetting
    // ------------------------------------------------------------
    public static Map<BlockPos, BlockState> offset(Map<BlockPos, BlockState> shape, BlockPos offset) {
        Map<BlockPos, BlockState> out = new HashMap<>();
        shape.forEach((pos, block) ->
                out.put(pos.offset(offset), block));
        return out;
    }

    // ------------------------------------------------------------
    // Rotation (Y-axis)
    // ------------------------------------------------------------
    public static Map<BlockPos, BlockState> rotate90(Map<BlockPos, BlockState> shape) {
        Map<BlockPos, BlockState> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos r = new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            out.put(r, block);
        });
        return out;
    }

    public static Map<BlockPos, BlockState> rotate180(Map<BlockPos, BlockState> shape) {
        return rotate90(rotate90(shape));
    }

    public static Map<BlockPos, BlockState> rotate270(Map<BlockPos, BlockState> shape) {
        return rotate90(rotate180(shape));
    }

    // ------------------------------------------------------------
    // Mirroring
    // ------------------------------------------------------------
    public static Map<BlockPos, BlockState> mirrorX(Map<BlockPos, BlockState> shape) {
        Map<BlockPos, BlockState> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos m = new BlockPos(-pos.getX(), pos.getY(), pos.getZ());
            out.put(m, block);
        });
        return out;
    }

    public static Map<BlockPos, BlockState> mirrorZ(Map<BlockPos, BlockState> shape) {
        Map<BlockPos, BlockState> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos m = new BlockPos(pos.getX(), pos.getY(), -pos.getZ());
            out.put(m, block);
        });
        return out;
    }

    // ------------------------------------------------------------
    // World matching
    // ------------------------------------------------------------
    public static boolean matches(Level level, BlockPos origin, Map<BlockPos, BlockState> shape) {
        for (Map.Entry<BlockPos, BlockState> e : shape.entrySet()) {
            BlockPos worldPos = origin.offset(e.getKey());
            BlockState expected = e.getValue();
            BlockState actual = level.getBlockState(worldPos);

            if (actual != expected)
                return false;
        }
        return true;
    }
    public static Tuple<BlockPos, BlockState> ConvertFromMapToTuple(Map<BlockPos, BlockState> blocks) {
        return new Tuple<>(blocks.keySet().iterator().next(), blocks.values().iterator().next());
    }

    public static Map<BlockPos, BlockState> ConvertFromTupleToMap(Tuple<BlockPos, BlockState> tuple) {
        Map<BlockPos, BlockState> result = new HashMap<>();
        result.put(tuple.getA(), tuple.getB());
        return result;
    }
}
