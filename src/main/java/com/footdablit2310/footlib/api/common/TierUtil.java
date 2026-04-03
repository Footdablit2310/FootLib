package com.footdablit2310.footlib.common;

import com.footdablit2310.footorganicprocessing.api.ptf.PTFTier;

public final class TierUtil {

    public static boolean isHigher(PTFTier a, PTFTier b) {
        return a.level() > b.level();
    }

    public static boolean isValid(int tier, int maxTier) {
        return tier >= 0 && tier <= maxTier;
    }
}
