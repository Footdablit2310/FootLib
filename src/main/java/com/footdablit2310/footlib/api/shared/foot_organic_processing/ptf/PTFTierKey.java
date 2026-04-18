package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

import net.minecraft.resources.ResourceLocation;

public record PTFTierKey(ResourceLocation id) {

    @Override
    public String toString() {
        return id.toString();
    }
}
