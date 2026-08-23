package com.footdablit2310.footlib.api.common.json_config;

import com.google.gson.JsonElement;

public abstract class ConfigType<T> {
	public abstract JsonElement toJson(T value);
	public abstract T fromJson(JsonElement element);
}

