package com.footdablit2310.footlib.api.rotation;

import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.Set;

/** Nullable-field patch applied over inherited rotation settings. */
public final class RotationSettingsOverride {
    private final Boolean enabled;
    private final Quaterniond rotation;
    private final Vector3d pivot;
    private final Boolean smooth;
    private final Integer interpolationTicks;
    private final Double rotationStepDegrees;
    private final Set<String> allowedAxes;

    public RotationSettingsOverride(Boolean enabled, Quaterniond rotation, Vector3d pivot,
            Boolean smooth, Integer interpolationTicks, Double rotationStepDegrees, Set<String> allowedAxes) {
        this.enabled = enabled;
        this.rotation = rotation == null ? null : new Quaterniond(rotation).normalize();
        this.pivot = pivot == null ? null : new Vector3d(pivot);
        this.smooth = smooth;
        this.interpolationTicks = interpolationTicks;
        this.rotationStepDegrees = rotationStepDegrees;
        this.allowedAxes = allowedAxes == null ? null : Set.copyOf(allowedAxes);
    }

    public RotationSettings apply(RotationSettings base) {
        return new RotationSettings(
                enabled == null ? base.enabled() : enabled,
                rotation == null ? base.rotation() : rotation,
                pivot == null ? base.pivot() : pivot,
                smooth == null ? base.smooth() : smooth,
                interpolationTicks == null ? base.interpolationTicks() : interpolationTicks,
                rotationStepDegrees == null ? base.rotationStepDegrees() : rotationStepDegrees,
                allowedAxes == null ? base.allowedAxes() : allowedAxes
        );
    }
}
