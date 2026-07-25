package com.footdablit2310.footlib.api.common.rcc_api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Data {
    private final Map<String, String> values = new HashMap<>();
    public Data(Map<String, String> dataMap) {
        values.putAll(dataMap);
    }
    public Data(){}
    public static Data appendDataFromMap(Map<String, String> dataMap) {
        Data data = new Data();
        data.values.putAll(dataMap);
        return data;
    }
    public Optional<Data> put(String key, String value, Boolean returnData) {
        values.put(key, value);
        if (returnData) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
    public void put(String key, String value) {
        values.put(key, value);
    }
    public String get(String key) { return values.get(key); }
    public Map<String, String> asMap() { return values; }
}
