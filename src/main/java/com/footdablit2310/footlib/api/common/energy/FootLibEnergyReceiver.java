package com.footdablit2310.footlib.api.common.energy;

public interface FootLibEnergyReceiver {

    /**
     * Receive energy into this block entity.
     *
     * @param maxReceive Maximum FE to receive
     * @param simulate   If true, do not modify stored energy
     * @return Amount of FE accepted
     */
    int footlib$receiveEnergy(int maxReceive, boolean simulate);

    /**
     * @return Current stored FE
     */
    int footlib$getStoredEnergy();

    /**
     * @return Maximum FE capacity
     */
    int footlib$getMaxEnergy();
}
