package com.footdablit2310.footlib.api.common.json_config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Default types for this Config System
 */
public final class Types {

	public static final ConfigType<Integer> INT = new ConfigType<>() {
		@Override public JsonElement toJson(Integer v) { return new JsonPrimitive(v); }
		@Override public Integer fromJson(JsonElement e) { return e.getAsInt(); }
	};

	public static final ConfigType<Boolean> BOOL = new ConfigType<>() {
		@Override public JsonElement toJson(Boolean v) { return new JsonPrimitive(v); }
		@Override public Boolean fromJson(JsonElement e) { return e.getAsBoolean(); }
	};

	public static final ConfigType<String> STRING = new ConfigType<>() {
		@Override public JsonElement toJson(String v) { return new JsonPrimitive(v); }
		@Override public String fromJson(JsonElement e) { return e.getAsString(); }
	};

	public static <T> ConfigType<List<T>> listOf(ConfigType<T> inner) {
		return new ConfigType<>() {
			@Override public JsonElement toJson(List<T> v) {
				JsonArray arr = new JsonArray();
				for (T item : v) arr.add(inner.toJson(item));
				return arr;
			}

			@Override public List<T> fromJson(JsonElement e) {
				List<T> list = new ArrayList<>();
				for (JsonElement el : e.getAsJsonArray()) list.add(inner.fromJson(el));
				return list;
			}
		};
	}
	public static final ConfigType<UUID> UUID = new ConfigType<>() {
		@Override
		public JsonElement toJson(UUID value) {
			return new JsonPrimitive(value.toString());
		}

		@Override
		public UUID fromJson(JsonElement element) {
			return java.util.UUID.fromString(element.getAsString());
		}
	};
}
