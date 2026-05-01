package com.footdablit2310.footlib.api.shared.foot_organic_processing.coils;

import com.footdablit2310.footlib.registry.registry.helpers.heat.HeatTier;

public record CoilRegistryFormat(String id, HeatTier heatTier, int resistance) {}