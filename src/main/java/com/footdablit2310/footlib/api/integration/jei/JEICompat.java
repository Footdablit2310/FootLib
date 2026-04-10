package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.api.common.ModPresence;

public final class JEICompat {

    private JEICompat() {}

    public static void init() {
        if (!ModPresence.isInstalled("jei")) {
            return;
        }
        // TODO: Add any static init logic for JEI compat here. This method is called from the main mod class during common setup.
        // Shared JEI helpers are stateless; nothing to init yet.
        // This is the hook if you ever need registration helpers later.
    }
}
