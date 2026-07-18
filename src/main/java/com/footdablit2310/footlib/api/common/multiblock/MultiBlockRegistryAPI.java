package com.footdablit2310.footlib.api.common.multiblock;

import com.footdablit2310.footlib.registry.MultiBlockRegistry;
import net.minecraft.resources.Identifier;

public final class MultiBlockRegistryAPI {

    private MultiBlockRegistryAPI() {
    }

    public static void register(Identifier id, MultiBlockDefinition def) {
        MultiBlockRegistry.register(id, def);
    }

    public static MultiBlockDefinition get(Identifier id) {
        return MultiBlockRegistry.get(id);
    }
}
