package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.api.common.ModPresence;

public final class JEICompat {

    private JEICompat() {}

    public static void init() {
        if (!ModPresence.isInstalled("jei")) return;

        // JEI helpers are stateless; nothing to initialize.
    }
}
