package com.footdablit2310.footlib.api.common.heat;

public record HeatInfo(String heat_name, int heat) {

    public static HeatInfo of(HeatTier tier, int temperature) {
        return new HeatInfo(tier.heatName(), temperature);
    }
}
