package com.footdablit2310.footlib.api.shared.foot_organic_processing;

import com.footdablit2310.footorganicprocessing.api.IFootOrganicAccess;
import com.footdablit2310.footorganicprocessing.api.ptf.IPTFTierAccess;

public class FootOrganicAccessImpl implements IFootOrganicAccess {

    private final IPTFTierAccess ptfTiers;

    public FootOrganicAccessImpl(IPTFTierAccess ptfTiers) {
        this.ptfTiers = ptfTiers;
    }

    @Override
    public IPTFTierAccess ptfTiers() {
        return ptfTiers;
    }
}
