package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.api.common.basic.ModPresence;

public final class JEICompat {

    private JEICompat() {}

    public static boolean JEIInstalled() {
        return ModPresence.isInstalled(ModPresence.JEI);
    }
    public static void init() {
        if (!JEIInstalled()) return;

        // JEI helpers are stateless; nothing to initialize.
    }
}
