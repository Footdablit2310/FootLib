package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

import java.util.Objects;

public final class PTFHeatLevelKey {

    private final String namespace;
    private final String path;

    public PTFHeatLevelKey(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String namespace() { return namespace; }
    public String path() { return path; }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PTFHeatLevelKey other)) return false;
        return namespace.equals(other.namespace) && path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }
}
