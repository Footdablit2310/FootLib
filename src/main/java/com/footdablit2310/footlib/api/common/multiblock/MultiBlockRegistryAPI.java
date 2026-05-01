package com.footdablit2310.footlib.api.common.multiblock;

import com.footdablit2310.footlib.registry.MultiBlockRegistry;
import net.minecraft.resources.ResourceLocation;

public final class MultiBlockRegistryAPI {

    private MultiBlockRegistryAPI() {}

    public static void register(ResourceLocation id, MultiBlockDefinition def) {
        MultiBlockRegistry.register(id, def);
    }

    public static MultiBlockDefinition get(ResourceLocation id) {
        return MultiBlockRegistry.get(id);
    }
}
