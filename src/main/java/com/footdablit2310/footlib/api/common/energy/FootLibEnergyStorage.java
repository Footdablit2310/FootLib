package com.footdablit2310.footlib.api.common.energy;

public class FootLibEnergyStorage {

    private int energy;
    private final int capacity;

    public FootLibEnergyStorage(int capacity) {
        this.capacity = capacity;
    }

    public int receive(int amount, boolean simulate) {
        if (amount <= 0)
            return 0;

        int accepted = Math.min(capacity - energy, amount);

        if (!simulate)
            energy += accepted;

        return accepted;
    }

    public int extract(int amount, boolean simulate) {
        if (amount <= 0)
            return 0;

        int extracted = Math.min(energy, amount);

        if (!simulate)
            energy -= extracted;

        return extracted;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setEnergy(int value) {
        energy = Math.min(capacity, Math.max(0, value));
    }
}
