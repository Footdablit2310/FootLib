package com.footdablit2310.footlib.api.rotation;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/** Immutable data-driven rules for one BlockEntity-backed block. */
public record RotationBlockConfig(
        ResourceLocation block,
        boolean enabled,
        double rotationStepDegrees,
        Set<String> allowedAxes,
        boolean smooth,
        int interpolationTicks
) {
    public RotationBlockConfig {
        allowedAxes = Set.copyOf(allowedAxes);
        if (!Double.isFinite(rotationStepDegrees) || rotationStepDegrees <= 0.0 || rotationStepDegrees > 360.0) {
            throw new IllegalArgumentException("rotation_step_degrees must be in (0, 360]");
        }
        if (interpolationTicks < 0 || interpolationTicks > 1200) {
            throw new IllegalArgumentException("interpolation_ticks must be between 0 and 1200");
        }
        if (allowedAxes.isEmpty() || !Set.of("x", "y", "z").containsAll(allowedAxes)) {
            throw new IllegalArgumentException("allowed_axes must contain one or more of x, y, z");
        }
    }

    public boolean allowsAxis(String axis) {
        return enabled && allowedAxes.contains(axis);
    }
}
