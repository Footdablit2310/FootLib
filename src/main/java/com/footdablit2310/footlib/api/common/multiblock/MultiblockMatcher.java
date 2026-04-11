package com.footdablit2310.footlib.api.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MultiblockMatcher {

    public static boolean matches(Level level, BlockPos core, MultiblockPattern pattern) {

        String[][] layers = pattern.layers();
        var keys = pattern.keys();

        int height = layers.length;

        for (int y = 0; y < height; y++) {
            String[] rows = layers[y];
            for (int z = 0; z < rows.length; z++) {
                String row = rows[z];
                for (int x = 0; x < row.length(); x++) {

                    char key = row.charAt(x);
                    Block expected = keys.get(key);
                    if (expected == null) continue;

                    BlockPos checkPos = core.offset(x - 1, y - 1, z - 1);
                    Block actual = level.getBlockState(checkPos).getBlock();

                    if (actual != expected) return false;
                }
            }
        }
        return true;
    }
}
