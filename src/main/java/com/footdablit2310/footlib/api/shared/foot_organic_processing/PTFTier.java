package com.footdablit2310.footlib.api.shared.foot_organic_processing;

public final class PTFTier {

    private final int level;
    private final int heatRadius;
    private final int superHeatRadius;

    public PTFTier(int level, int heatRadius, int superHeatRadius) {
        this.level = level;
        this.heatRadius = heatRadius;
        this.superHeatRadius = superHeatRadius;
    }

    public int level() {
        return level;
    }

    public int heatRadius() {
        return heatRadius;
    }

    public int superHeatRadius() {
        return superHeatRadius;
    }
}
