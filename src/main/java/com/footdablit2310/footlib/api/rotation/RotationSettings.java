package com.footdablit2310.footlib.api.rotation;

import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.Set;

/** Immutable, resolved settings for one BlockState. */
public final class RotationSettings {
    private final boolean enabled;
    private final Quaterniond rotation;
    private final Vector3d pivot;
    private final boolean smooth;
    private final int interpolationTicks;
    private final double rotationStepDegrees;
    private final Set<String> allowedAxes;

    public RotationSettings(boolean enabled, Quaterniond rotation, Vector3d pivot,
            boolean smooth, int interpolationTicks, double rotationStepDegrees, Set<String> allowedAxes) {
        this.enabled = enabled;
        if (rotation == null || !Double.isFinite(rotation.lengthSquared()) || rotation.lengthSquared() < 1.0E-24) {
            throw new IllegalArgumentException("rotation quaternion must be finite and non-zero");
        }
        if (pivot == null || allowedAxes == null) {
            throw new IllegalArgumentException("pivot and allowedAxes cannot be null");
        }
        this.rotation = new Quaterniond(rotation).normalize();
        this.pivot = new Vector3d(pivot);
        this.smooth = smooth;
        this.interpolationTicks = interpolationTicks;
        this.rotationStepDegrees = rotationStepDegrees;
        this.allowedAxes = Set.copyOf(allowedAxes);
        if (!Double.isFinite(rotationStepDegrees) || rotationStepDegrees <= 0.0 || rotationStepDegrees >= 360.0)
            throw new IllegalArgumentException("rotation_step_degrees must be in (0, 360)");
        if (interpolationTicks < 0 || interpolationTicks > 1200)
            throw new IllegalArgumentException("interpolation_ticks must be between 0 and 1200");
        if (this.allowedAxes.isEmpty() || !Set.of("x", "y", "z", "w").containsAll(this.allowedAxes))
            throw new IllegalArgumentException("allowed_axes must contain one or more of x, y, z, w");
        if (!Double.isFinite(pivot.x) || !Double.isFinite(pivot.y) || !Double.isFinite(pivot.z))
            throw new IllegalArgumentException("pivot coordinates must be finite");
    }

    public boolean enabled() { return enabled; }
    public Quaterniond rotation() { return new Quaterniond(rotation); }
    public Vector3d pivot() { return new Vector3d(pivot); }
    public boolean smooth() { return smooth; }
    public int interpolationTicks() { return interpolationTicks; }
    public double rotationStepDegrees() { return rotationStepDegrees; }
    public Set<String> allowedAxes() { return allowedAxes; }
    public boolean allowsAxis(String axis) { return enabled && allowedAxes.contains(axis); }
}
