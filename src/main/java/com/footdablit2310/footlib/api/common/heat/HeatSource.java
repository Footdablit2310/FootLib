package com.footdablit2310.footlib.api.common.heat;

public interface HeatSource {

    /**
     * Constant heat output in °C.
     */
    int heatOutput();

    /**
     * Tier derived from the constant heat output.
     */
    default HeatTier outputTier() {
        return HeatTier.fromTemperature(heatOutput());
    }

    /**
     * External-friendly struct.
     */
    default HeatInfo toHeatInfo() {
        return new HeatInfo(outputTier().heatName(), heatOutput());
    }
}
