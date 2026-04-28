package com.footdablit2310.footlib.api.registry.helpers.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

/**
 * Universal multiblock validator for ANY shape:
 * - ShapeCoords
 *
 * Supports:
 * - Rotation (0/90/180/270)
 * - Mirroring (X/Z)
 * - Offsets
 * - Full world validation
 *
 * FootLib has NO hard dependency on any other mod.
 */
public class MultiBlockChecker {

    // ------------------------------------------------------------
    // Result object
    // ------------------------------------------------------------
    public record Result(boolean valid, BlockPos origin, int rotation, boolean mirrored) {}

    // ------------------------------------------------------------
    // Main check method
    // ------------------------------------------------------------
    public static Result check(Level level, BlockPos controllerPos, ShapeCoords shape) {

        // Convert ANY shape → coordinate map
        Map<BlockPos, BlockState> base = shape.blocks();

        // Try all rotations
        for (int rot = 0; rot < 4; rot++) {

            Map<BlockPos, BlockState> rotated = switch (rot) {
                case 1 -> MultiBlockHelpers.rotate90(base);
                case 2 -> MultiBlockHelpers.rotate180(base);
                case 3 -> MultiBlockHelpers.rotate270(base);
                default -> base;
            };

            // Try mirrored and non-mirrored
            for (boolean mirror : new boolean[]{false, true}) {

                Map<BlockPos, BlockState> variant = mirror
                        ? MultiBlockHelpers.mirrorX(rotated)
                        : rotated;

                // Normalize shape so controller is at (0,0,0)
                Map<BlockPos, BlockState> normalized = normalize(variant);

                // Check match
                if (MultiBlockHelpers.matches(level, controllerPos, normalized)) {
                    return new Result(true, controllerPos, rot * 90, mirror);
                }
            }
        }

        return new Result(false, controllerPos, 0, false);
    }

    // ------------------------------------------------------------
    // Normalize shape so minimum coordinate becomes (0,0,0)
    // ------------------------------------------------------------
    private static Map<BlockPos, BlockState> normalize(Map<BlockPos, BlockState> shape) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (BlockPos pos : shape.keySet()) {
            if (pos.getX() < minX) minX = pos.getX();
            if (pos.getY() < minY) minY = pos.getY();
            if (pos.getZ() < minZ) minZ = pos.getZ();
        }

        BlockPos offset = new BlockPos(-minX, -minY, -minZ);

        Map<BlockPos, BlockState> out = new HashMap<>();
        shape.forEach((pos, block) ->
                out.put(pos.offset(offset), block));

        return out;
    }
}
