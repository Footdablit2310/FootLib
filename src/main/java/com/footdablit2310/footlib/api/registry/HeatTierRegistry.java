package com.footdablit2310.footlib.api.registry;

import com.footdablit2310.footlib.api.registry.helpers.HeatTier;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HeatTierRegistry {

    private static final Map<String, HeatTier> TIERS = new LinkedHashMap<>();

    public static HeatTier register(String id, int strength) {
        HeatTier tier = new HeatTier(id, strength);
        TIERS.put(id, tier);
        return tier;
    }

    public static HeatTier get(String id) {
        return TIERS.getOrDefault(id, TIERS.get("none"));
    }

    public static int strengthOf(String id) {
        return get(id).strength();
    }

    public static Map<String, HeatTier> all() {
        return TIERS;
    }
}
