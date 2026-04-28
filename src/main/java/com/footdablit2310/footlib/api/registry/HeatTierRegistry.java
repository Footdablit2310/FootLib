package com.footdablit2310.footlib.api.registry;

import java.util.LinkedHashMap;
import java.util.Map;

import com.footdablit2310.footlib.api.registry.helpers.heat.HeatTier;

public final class HeatTierRegistry {

    private static final Map<String, HeatTier> TIERS = new LinkedHashMap<>();

    public static HeatTier register(String id, int heat) {
        HeatTier tier = new HeatTier(id, heat);
        TIERS.put(id, tier);
        return tier;
    }
    public static final HeatTier NONE = register("none", 0);
    public static final HeatTier HEATED = register("heated", 750);
    public static final HeatTier SUPERHEATED = register("superheated", 1500);
    public static final HeatTier ULTRAHEATED = register("ultraheated", 3000);
    // -R tiers have stronger resistance but same output.

    public static HeatTier get(String id) {
        return TIERS.getOrDefault(id, NONE);
    }

    public static int heatOf(String id) {
        return get(id).heat();
    }

    public static Map<String, HeatTier> all() {
        return TIERS;
    }
}
