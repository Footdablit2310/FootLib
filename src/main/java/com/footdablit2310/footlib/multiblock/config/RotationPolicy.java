package com.footdablit2310.footlib.multiblock.config;

import java.util.List;

public class RotationPolicy {

    private final Angles<Angle> single;
    private final List<Angles<Angle>> multi;

    private RotationPolicy(Angles<Angle> single, List<Angles<Angle>> multi) {
        this.single = single;
        this.multi = multi;
    }

    public static RotationPolicy single(Angles<Angle> angles) {
        return new RotationPolicy(angles, null);
    }

    public static RotationPolicy multi(List<Angles<Angle>> angles) {
        return new RotationPolicy(null, angles);
    }

    public boolean isSingle() { return single != null; }
    public boolean isMulti() { return multi != null; }

    public Angles<Angle> asSingle() { return single; }
    public List<Angles<Angle>> asMulti() { return multi; }
}
