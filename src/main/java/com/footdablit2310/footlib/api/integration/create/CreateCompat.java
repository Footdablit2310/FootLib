package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.ModPresence;

public final class CreateCompat {

    private CreateCompat() {}

    public static boolean CreateInstalled() {
        return ModPresence.isInstalled(ModPresence.CREATE);
    }
    public static void init() {
        if (!CreateInstalled()) {
            return;
        }
        // TODO: Add any static init logic for Create compat here. This method is called from the main mod class during common setup.
        // If you ever need static init logic for builders, do it here.
        // For now, builders are just plain classes, so nothing required.
    }
}
