package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

/**
 * Represents a heat level in the PTF system.
 * Fully extensible by other mods.
 */
public interface IPTFHeatLevel {

    /** Unique identifier */
    PTFHeatLevelKey key();

    /** Temperature in °C */
    int temperature();

    /** Display name for JEI/EMI */
    String displayName();
}
