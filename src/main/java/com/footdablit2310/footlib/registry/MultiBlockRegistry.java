package com.footdablit2310.footlib.registry;

import com.footdablit2310.footlib.api.common.multiblock.MultiBlockDefinition;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class MultiBlockRegistry {

    private static final Map<Identifier, MultiBlockDefinition> REGISTRY = new HashMap<>();

    private MultiBlockRegistry() {
    }

    public static void register(Identifier id, MultiBlockDefinition def) {
        REGISTRY.put(id, def);
    }

    public static MultiBlockDefinition get(Identifier id) {
        return REGISTRY.get(id);
    }
}
