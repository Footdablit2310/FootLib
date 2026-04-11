package com.footdablit2310.footlib.api.common.multiblock;

import net.minecraft.world.level.block.Block;
import java.util.*;

public class MultiblockPattern {

    private final String[][] layers;
    private final Map<Character, Block> keys;

    public MultiblockPattern(String[][] layers, Map<Character, Block> keys) {
        this.layers = layers;
        this.keys = keys;
    }

    public String[][] layers() { return layers; }
    public Map<Character, Block> keys() { return keys; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final List<String[]> layers = new ArrayList<>();
        private final Map<Character, Block> keys = new HashMap<>();

        public Builder layer(String... rows) {
            layers.add(rows);
            return this;
        }

        public Builder key(char c, Block block) {
            keys.put(c, block);
            return this;
        }

        public MultiblockPattern build() {
            return new MultiblockPattern(
                    layers.toArray(new String[0][]),
                    keys
            );
        }
    }
}
