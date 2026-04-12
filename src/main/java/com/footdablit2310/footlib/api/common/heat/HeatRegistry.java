package com.footdablit2310.footlib.api.common.heat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HeatRegistry {

    private static final Logger LOGGER = LogManager.getLogger("FootLib-HeatRegistry");

    private static final Map<String, HeatTierData> REGISTRY = new HashMap<>();
    private static final Map<String, HeatTierData> BUILTIN = new HashMap<>();

    static {
        LOGGER.info("[FootLib:HeatRegistry] Registering built-in heat tiers...");

        for (HeatTier tier : HeatTier.values()) {
            HeatTierData data = new HeatTierData(
                    tier.heatName(),
                    tier.heat(),
                    tier.heatResistance(),
                    tier.isReinforced()
            );

            BUILTIN.put(tier.heatName(), data);
            REGISTRY.put(tier.heatName(), data);

            LOGGER.info("  • Built-in tier registered: {} (heat={}, resistance={}, reinforced={})",
                    tier.heatName(), tier.heat(), tier.heatResistance(), tier.isReinforced());
        }

        LOGGER.info("[FootLib:HeatRegistry] Built-in heat tiers loaded successfully.");
    }

    /**
     * Register a custom tier.
     * Built-in tiers cannot be overridden.
     */
    public static void register(String id, HeatTierData data) {
        if (BUILTIN.containsKey(id)) {
            LOGGER.warn("[FootLib:HeatRegistry] Attempted override of built-in tier '{}'. This is not allowed. Ignoring.", id);
            return;
        }

        boolean replacing = REGISTRY.containsKey(id);

        REGISTRY.put(id, data);

        if (replacing) {
            LOGGER.info("[FootLib:HeatRegistry] Custom tier '{}' overridden (heat={}, resistance={}, reinforced={})",
                    id, data.heat(), data.resistance(), data.reinforced());
        } else {
            LOGGER.info("[FootLib:HeatRegistry] Custom tier '{}' registered (heat={}, resistance={}, reinforced={})",
                    id, data.heat(), data.resistance(), data.reinforced());
        }
    }

    public static HeatTierData get(String id) {
        return REGISTRY.get(id);
    }

    public static Map<String, HeatTierData> all() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static boolean isBuiltin(String id) {
        return BUILTIN.containsKey(id);
    }
}
