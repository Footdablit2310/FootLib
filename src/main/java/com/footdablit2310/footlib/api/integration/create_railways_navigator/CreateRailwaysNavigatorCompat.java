package com.footdablit2310.footlib.api.integration.create_railways_navigator;

import com.footdablit2310.footlib.api.common.basic.ModPresence;

public final class CreateRailwaysNavigatorCompat {
    public static boolean CreateRailwaysNavigatorInstalled() {
        return ModPresence.isInstalled(ModPresence.CREATE_RAILWAYS_NAVIGATOR);
    }

    public static void init() {
        if (!CreateRailwaysNavigatorInstalled()) {
            return;
        }
        //TODO: Add init logic
    }
}
