package com.footdablit2310.footlib.api.shared.foot_organic_processing.coils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.footdablit2310.footlib.registry.HeatTierRegistry;
import com.footdablit2310.footlib.registry.registry.helpers.heat.HeatTier;

public class CoilRegistry {
    private static final Map<String, CoilRegistryFormat> COILS = new LinkedHashMap<>();

    public static CoilRegistryFormat register(String id, HeatTier heatTier, int resistance) {
        CoilRegistryFormat coil = new CoilRegistryFormat(id, heatTier, resistance);
        COILS.put(id, coil);
        return coil;
    }

    public static CoilRegistryFormat get(String id) {
        return COILS.get(id);
    }

    public static final CoilRegistryFormat HEATED = register("heated", HeatTierRegistry.HEATED, 1500);
    public static final CoilRegistryFormat SUPERHEATED = register("superheated", HeatTierRegistry.SUPERHEATED, 2250);
    public static final CoilRegistryFormat ULTRAHEATED = register("ultraheated", HeatTierRegistry.ULTRAHEATED, 3000);
    //public static final CoilRegistryFormat SUPERHEATED_R = register("superheated_r", HeatTierRegistry.SUPERHEATED, 4500);
    public static final CoilRegistryFormat ULTRAHEATED_R = register("ultraheated_r", HeatTierRegistry.ULTRAHEATED, 12000);

    public static Map<String, CoilRegistryFormat> all() {
        return COILS;
    }
}
