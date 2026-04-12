package com.footdablit2310.footlib.api.integration.emi;

import com.footdablit2310.footlib.api.common.ModPresence;

public final class EMICompat {
    public static boolean EMIInstalled() {
        return ModPresence.isInstalled(ModPresence.EMI);
    }

    public static void init() {
        if (!EMIInstalled()) {
            return;
        }
        //TODO: Add init logic
    }    
}
