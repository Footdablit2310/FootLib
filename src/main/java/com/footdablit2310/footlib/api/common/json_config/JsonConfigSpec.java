package com.footdablit2310.footlib.api.common.json_config;

import java.util.Map;

public final class JsonConfigSpec {

	public final String path;
	public final Map<String, ConfigValue<?>> values;

	public JsonConfigSpec(String path, Map<String, ConfigValue<?>> values) {
		this.path = path;
		this.values = values;
	}
}

