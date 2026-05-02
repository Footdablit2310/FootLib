package com.footdablit2310.footlib.api.integration.create;

public enum FootLibHeatLevel {
    NONE,
    HEATED,
    SUPERHEATED;

    public static FootLibHeatLevel fromTemperature(int tempC) {
        if (tempC >= 1500)
            return SUPERHEATED;
        if (tempC >= 750)
            return HEATED;
        return NONE;
    }
}
