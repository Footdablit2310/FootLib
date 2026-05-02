package com.footdablit2310.footlib.api.common.basic;

public final class RadiusUtil {

    public static boolean withinRadius(int dx, int dz, int radius) {
        return (dx * dx + dz * dz) <= radius * radius;
    }

    public static int area(int radius) {
        return (int) (Math.PI * radius * radius);
    }
}
