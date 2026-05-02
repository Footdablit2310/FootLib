package com.footdablit2310.footlib.api.common.redstone;

public interface FootLibRedstoneConfigurable {

    FootLibRedstoneMode footlib$getRedstoneMode();
    void footlib$setRedstoneMode(FootLibRedstoneMode mode);

    default boolean footlib$isRedstoneActive(boolean powered, boolean pulseTriggered) {
        return footlib$getRedstoneMode().isActive(powered, pulseTriggered);
    }
}
