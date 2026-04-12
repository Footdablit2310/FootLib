package com.footdablit2310.footlib.api.common.heat;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum HeatTier implements StringRepresentable {

    NONE("none", 0, 125, false),
    HEATED("heated", 750, 1500, false),
    SUPERHEATED("superheated", 1500, 2250, false),
    ULTRAHEATED("ultraheated", 3000, 3000, false),

    // Reinforced variants
    HEATED_R("heated-r", HEATED.heat(), HEATED.heatResistance()*4, true),
    SUPERHEATED_R("superheated-r", SUPERHEATED.heat(), SUPERHEATED.heatResistance()*4, true),
    ULTRAHEATED_R("ultraheated-r", ULTRAHEATED.heat(), ULTRAHEATED.heatResistance()*4, true);

    private final String heatName;
    private final int heat;               // constant heat output
    private final int baseResistance;     // base survivability
    private final boolean reinforced;     // reinforcement flag

    HeatTier(String heatName, int heat, int baseResistance, boolean reinforced) {
        this.heatName = heatName;
        this.heat = heat;
        this.baseResistance = baseResistance;
        this.reinforced = reinforced;
    }

    public String heatName() {
        return heatName;
    }

    public int heat() {
        return heat;
    }

    public boolean isReinforced() {
        return reinforced;
    }

    public int heatResistance() {
        return reinforced ? baseResistance * 4 : baseResistance;
    }

    public static final Codec<HeatTier> CODEC =
            StringRepresentable.fromEnum(HeatTier::values);

    @Override
    public String getSerializedName() {
        return heatName;
    }
    public static HeatTier fromTemperature(int temp) {
    if (temp >= ULTRAHEATED.heat()) return ULTRAHEATED;
    if (temp >= SUPERHEATED.heat()) return SUPERHEATED;
    if (temp >= HEATED.heat()) return HEATED;
    return NONE;
    }

}
