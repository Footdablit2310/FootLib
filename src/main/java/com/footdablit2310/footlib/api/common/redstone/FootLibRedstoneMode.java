package com.footdablit2310.footlib.api.common.redstone;

public enum FootLibRedstoneMode {
    IGNORE,     // Always active
    HIGH,       // Active only when powered
    LOW,        // Active only when NOT powered
    PULSE;      // Active only on rising edge

    public boolean isActive(boolean powered, boolean pulseTriggered) {
        return switch (this) {
            case IGNORE -> true;
            case HIGH -> powered;
            case LOW -> !powered;
            case PULSE -> pulseTriggered;
        };
    }
}
