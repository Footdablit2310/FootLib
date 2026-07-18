package com.footdablit2310.footlib.api.common.rcc_api;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private final Map<String, String> values = new HashMap<>();

    public void put(String key, String value) { values.put(key, value); }
    public String get(String key) { return values.get(key); }
    public Map<String, String> asMap() { return values; }
}
