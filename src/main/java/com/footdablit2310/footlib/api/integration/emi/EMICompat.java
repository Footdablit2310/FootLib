package com.footdablit2310.footlib.api.integration.emi;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.api.common.basic.ModPresence;

public final class EMICompat {
    public static boolean EMIInstalled() {
        return ModPresence.isInstalled(ModPresence.EMI);
    }

    public static void init() {
        if (!EMIInstalled()) {
            FootLib.LOGGER.info("FootLib: EMI not detected, skipping EMI init in mod [FootLib].");
            return;
        }
        //TODO: Add init logic
    }    
}
