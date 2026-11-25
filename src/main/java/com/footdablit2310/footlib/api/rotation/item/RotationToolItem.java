package com.footdablit2310.footlib.api.rotation.item;

import net.minecraft.world.item.Item;

/** A quaternion editor tool; precision is expressed in degrees. */
public final class RotationToolItem extends Item {
    private final double precisionDegrees;

    public RotationToolItem(Properties properties, double precisionDegrees) {
        super(properties);
        if (!Double.isFinite(precisionDegrees) || precisionDegrees <= 0.0 || precisionDegrees >= 360.0) {
            throw new IllegalArgumentException("precisionDegrees must be in (0, 360)");
        }
        this.precisionDegrees = precisionDegrees;
    }

    public double precisionDegrees() {
        return precisionDegrees;
    }
}
