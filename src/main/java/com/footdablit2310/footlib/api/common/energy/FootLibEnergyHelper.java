package com.footdablit2310.footlib.api.common.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class FootLibEnergyHelper {

    /**
     * Wrap a FootLibEnergyReceiver into a NeoForge IEnergyStorage.
     * This allows NeoForge cables, generators, etc. to interact with FootLib machines.
     */
    public static IEnergyStorage wrap(FootLibEnergyReceiver receiver) {
        return new IEnergyStorage() {

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return receiver.footlib$receiveEnergy(maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                // FootLib machines do not output energy by default
                return 0;
            }

            @Override
            public int getEnergyStored() {
                return receiver.footlib$getStoredEnergy();
            }

            @Override
            public int getMaxEnergyStored() {
                return receiver.footlib$getMaxEnergy();
            }

            @Override
            public boolean canExtract() {
                return false;
            }

            @Override
            public boolean canReceive() {
                return true;
            }
        };
    }
}
