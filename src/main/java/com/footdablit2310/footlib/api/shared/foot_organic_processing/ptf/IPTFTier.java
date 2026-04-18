package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

public interface IPTFTier {

    int gridSize();

    int heatedZones();

    int superheatedZones();

    int ultraheatedZones();

    /**
     * Total FE/t cost for running this tier.
     */
    int fePerTick();
}
