package com.footdablit2310.footlib.api.common.heat;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum HeatTier implements StringRepresentable {
    NONE,
    HEATED,
    SUPERHEATED,
    ULTRAHEATED;

    public static final Codec<HeatTier> CODEC =
            StringRepresentable.fromEnum(HeatTier::values);

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
