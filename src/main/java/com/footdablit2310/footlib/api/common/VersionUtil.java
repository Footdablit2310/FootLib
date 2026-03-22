package com.footdablit2310.footlib.api.common;

import net.neoforged.fml.ModList;

public final class VersionUtil {

    private VersionUtil() {}

    public static String getVersion(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(c -> c.getModInfo().getVersion().toString())
                .orElse("0.0.0");
    }
}