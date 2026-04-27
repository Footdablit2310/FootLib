package com.footdablit2310.footlib.api.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
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
    // Convert ANY shape to coordinate map
    // ------------------------------------------------------------
    public static Map<BlockPos, Block> toCoords(MultiBlockShape shape) {
        if (shape instanceof ShapeCoords coords)
            return convertCoords(coords);

        if (shape instanceof Shape2D s2)
            return convert2D(s2);

        if (shape instanceof Shape3D s3)
            return convert3D(s3);

        return Map.of();
    }

    private static Map<BlockPos, Block> convertCoords(ShapeCoords shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        shape.blocks().forEach((list, block) ->
                out.put(new BlockPos(list.get(0), list.get(1), list.get(2)), block));
        return out;
    }

    private static Map<BlockPos, Block> convert2D(Shape2D shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        List<List<Block>> grid = shape.grid();

        for (int z = 0; z < grid.size(); z++) {
            List<Block> row = grid.get(z);
            for (int x = 0; x < row.size(); x++) {
                Block b = row.get(x);
                if (b != null)
                    out.put(new BlockPos(x, 0, z), b);
            }
        }
        return out;
    }

    private static Map<BlockPos, Block> convert3D(Shape3D shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        List<List<List<Block>>> grid = shape.grid();

        for (int y = 0; y < grid.size(); y++) {
            List<List<Block>> layer = grid.get(y);
            for (int z = 0; z < layer.size(); z++) {
                List<Block> row = layer.get(z);
                for (int x = 0; x < row.size(); x++) {
                    Block b = row.get(x);
                    if (b != null)
                        out.put(new BlockPos(x, y, z), b);
                }
            }
        }
        return out;
    }

    // ------------------------------------------------------------
    // Offsetting
    // ------------------------------------------------------------
    public static Map<BlockPos, Block> offset(Map<BlockPos, Block> shape, BlockPos offset) {
        Map<BlockPos, Block> out = new HashMap<>();
        shape.forEach((pos, block) ->
                out.put(pos.offset(offset), block));
        return out;
    }

    // ------------------------------------------------------------
    // Rotation (Y-axis)
    // ------------------------------------------------------------
    public static Map<BlockPos, Block> rotate90(Map<BlockPos, Block> shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos r = new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            out.put(r, block);
        });
        return out;
    }

    public static Map<BlockPos, Block> rotate180(Map<BlockPos, Block> shape) {
        return rotate90(rotate90(shape));
    }

    public static Map<BlockPos, Block> rotate270(Map<BlockPos, Block> shape) {
        return rotate90(rotate180(shape));
    }

    // ------------------------------------------------------------
    // Mirroring
    // ------------------------------------------------------------
    public static Map<BlockPos, Block> mirrorX(Map<BlockPos, Block> shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos m = new BlockPos(-pos.getX(), pos.getY(), pos.getZ());
            out.put(m, block);
        });
        return out;
    }

    public static Map<BlockPos, Block> mirrorZ(Map<BlockPos, Block> shape) {
        Map<BlockPos, Block> out = new HashMap<>();
        shape.forEach((pos, block) -> {
            BlockPos m = new BlockPos(pos.getX(), pos.getY(), -pos.getZ());
            out.put(m, block);
        });
        return out;
    }

    // ------------------------------------------------------------
    // World matching
    // ------------------------------------------------------------
    public static boolean matches(Level level, BlockPos origin, Map<BlockPos, Block> shape) {
        for (Map.Entry<BlockPos, Block> e : shape.entrySet()) {
            BlockPos worldPos = origin.offset(e.getKey());
            Block expected = e.getValue();
            Block actual = level.getBlockState(worldPos).getBlock();

            if (actual != expected)
                return false;
        }
        return true;
    }

    // ------------------------------------------------------------
    // Debug ASCII (optional)
    // ------------------------------------------------------------
    public static String ascii2D(Shape2D shape) {
        StringBuilder sb = new StringBuilder();
        List<List<Block>> grid = shape.grid();

        for (List<Block> row : grid) {
            for (Block b : row) {
                sb.append(b == null ? "." : "#");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
