package com.footdablit2310.footlib.registry.helpers.heat;

import com.footdablit2310.footlib.registry.HeatTierRegistry;

public final class HeatRegistryHelper {

    private HeatRegistryHelper() {}

    public static void registerDefaultHeatTiers() {
        HeatTierRegistry.register("none", 0);
        HeatTierRegistry.register("heated", 1);
        HeatTierRegistry.register("superheated", 2);
        HeatTierRegistry.register("ultraheated", 3);
    }
}
