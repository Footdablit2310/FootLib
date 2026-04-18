package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

public final class PTFTierBuilder {

    private int gridSize;
    private int heatedZones;
    private int superheatedZones;
    private int ultraheatedZones;
    private int fePerTick;

    private PTFTierBuilder() {}

    public static PTFTierBuilder create() {
        return new PTFTierBuilder();
    }

    public PTFTierBuilder gridSize(int gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    public PTFTierBuilder heatedZones(int heatedZones) {
        this.heatedZones = heatedZones;
        return this;
    }

    public PTFTierBuilder superheatedZones(int superheatedZones) {
        this.superheatedZones = superheatedZones;
        return this;
    }

    public PTFTierBuilder ultraheatedZones(int ultraheatedZones) {
        this.ultraheatedZones = ultraheatedZones;
        return this;
    }

    public PTFTierBuilder fePerTick(int fePerTick) {
        this.fePerTick = fePerTick;
        return this;
    }

    public IPTFTier build() {
        return new SimplePTFTier(gridSize, heatedZones, superheatedZones, ultraheatedZones, fePerTick);
    }

    private record SimplePTFTier(
            int gridSize,
            int heatedZones,
            int superheatedZones,
            int ultraheatedZones,
            int fePerTick
    ) implements IPTFTier {}
}
