package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.ModPresence;
import com.footdablit2310.footlib.registry.HeatTierRegistry;
import com.footdablit2310.footlib.registry.helpers.heat.HeatTier;

import java.util.Optional;

public class CreateHeatIntegration {

    private static boolean initialized = false;

    public static void init() {
        if (initialized)
            return;

        if (!CreateCompat.CreateInstalled())
            return;

        initialized = true;

        // Only register kinetic→heat bridges if Foot: Electricity is present
        if (ModPresence.isInstalled(ModPresence.FOOT_ELECTRICITY)) {
            registerKineticHeatBridge();
        }
    }

    /**
     * This is ONLY a hook.
     * Foot: Electricity will implement the actual conversion.
     */
    private static void registerKineticHeatBridge() {
        // FootElectricity will inject its converter here.
        // FootLib does NOT implement this.
    }

    /**
     * Map FootLib HeatTier → Create heat ID.
     * Only HEATED and SUPERHEATED exist in Create.
     */
    public static Optional<String> toCreateHeatId(HeatTier tier) {
        if (tier == HeatTierRegistry.HEATED) {
            return Optional.of("create:heated"); // HEATED is 750C in Create, which is the same as FootLib's HEATED tier.
        } else if (tier == HeatTierRegistry.SUPERHEATED) {
            return Optional.of("create:superheated"); // SUPERHEATED is 1500C in Create, which is the same as FootLib's SUPERHEATED tier.
        } else {
            return Optional.empty(); // NONE, ULTRAHEATED, ULTRAHEATED_R
        }
    }
}
