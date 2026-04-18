package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PTFTierRegistry {

    private static final Map<ResourceLocation, IPTFTier> TIERS = new LinkedHashMap<>();

    private PTFTierRegistry() {}

    public static void register(ResourceLocation id, IPTFTier tier) {
        TIERS.put(id, tier);
    }

    public static IPTFTier get(ResourceLocation id) {
        return TIERS.get(id);
    }

    public static Collection<IPTFTier> all() {
        return Collections.unmodifiableCollection(TIERS.values());
    }

    public static boolean contains(ResourceLocation id) {
        return TIERS.containsKey(id);
    }
}
