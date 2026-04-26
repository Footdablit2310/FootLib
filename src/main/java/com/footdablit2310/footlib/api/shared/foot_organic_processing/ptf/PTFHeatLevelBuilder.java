package com.footdablit2310.footlib.api.shared.foot_organic_processing.ptf;

public class PTFHeatLevelBuilder {

    private final PTFHeatLevelKey key;
    private int temperature;
    private String displayName;

    public PTFHeatLevelBuilder(PTFHeatLevelKey key) {
        this.key = key;
    }

    public static PTFHeatLevelBuilder create(String namespace, String path) {
        return new PTFHeatLevelBuilder(new PTFHeatLevelKey(namespace, path));
    }

    public PTFHeatLevelBuilder temperature(int temp) {
        this.temperature = temp;
        return this;
    }

    public PTFHeatLevelBuilder displayName(String name) {
        this.displayName = name;
        return this;
    }

    public IPTFHeatLevel build() {
        return new IPTFHeatLevel() {
            @Override public PTFHeatLevelKey key() { return key; }
            @Override public int temperature() { return temperature; }
            @Override public String displayName() { return displayName; }
        };
    }
}
