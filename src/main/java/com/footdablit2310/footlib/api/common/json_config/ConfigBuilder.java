package com.footdablit2310.footlib.api.common.json_config;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigBuilder {

	public static final class Properties {
		public String path;
		public Properties path(String p) { this.path = p; return this; }
	}

	private final Properties props;
	private final Map<String, ConfigValue<?>> values = new LinkedHashMap<>();

	public ConfigBuilder(Properties props) {
		this.props = props;
	}

	public void define(ConfigValue<?> value) {
		values.put(value.key, value);
	}

	public JsonConfigSpec build() {
		return new JsonConfigSpec(props.path, values);
	}
}
