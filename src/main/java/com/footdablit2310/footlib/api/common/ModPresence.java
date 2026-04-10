package com.footdablit2310.footlib.api.common;

import net.neoforged.fml.ModList;

public final class ModPresence {

    private ModPresence() {}

    /**
     * Universal mod presence checker.
     * Usage:
     *   ModPresence.isInstalled("create")
     *   ModPresence.isInstalled("jei")
     *   ModPresence.isInstalled("footorganicprocessing")
     */
    public static boolean isInstalled(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
