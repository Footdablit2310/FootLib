package com.footdablit2310.footlib.api.common;

public final class HeatUtil {

    public static int heatFalloff(int distance, int maxRadius) {
        if (distance > maxRadius) return 0;
        return maxRadius - distance;
    }

    public static boolean isHeated(int distance, int heatRadius) {
        return distance <= heatRadius;
    }

    public static boolean isSuperHeated(int distance, int shRadius) {
        return distance <= shRadius;
    }
}
