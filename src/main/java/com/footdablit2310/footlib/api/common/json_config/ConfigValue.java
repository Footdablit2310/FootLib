package com.footdablit2310.footlib.api.common.json_config;

public final class ConfigValue<T> {
	public final String key;
	public final ConfigType<T> type;
	public final T defaultValue;
	public T value;

	public ConfigValue(String key, ConfigType<T> type, T defaultValue) {
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}
}
