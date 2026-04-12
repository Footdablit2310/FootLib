package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.heat.HeatTier;
import com.footdablit2310.footlib.api.common.heat.HeatInfo;
import com.footdablit2310.footlib.api.common.heat.HeatSource;
import com.footdablit2310.footlib.api.common.ModPresence;

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
        return switch (tier) {
            case HEATED, HEATED_R -> Optional.of("create:heated");
            case SUPERHEATED, SUPERHEATED_R -> Optional.of("create:superheated");
            default -> Optional.empty(); // NONE, ULTRAHEATED, ULTRAHEATED_R
        };
    }

    public static HeatInfo toHeatInfo(HeatTier tier) {
        return new HeatInfo(tier.heatName(), tier.heat());
    }

    public static HeatInfo toHeatInfo(HeatSource source) {
        return new HeatInfo(
                source.outputTier().heatName(),
                source.heatOutput()
        );
    }
}
