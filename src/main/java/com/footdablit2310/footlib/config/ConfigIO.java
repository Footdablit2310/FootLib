package com.footdablit2310.footlib.config;

import com.footdablit2310.footlib.api.common.json_config.ConfigValue;
import com.footdablit2310.footlib.api.common.json_config.JsonConfigSpec;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class ConfigIO {

	public static void load(JsonConfigSpec spec) {
		Path path = Path.of(spec.path);

		try {
			if (!Files.exists(path)) {
				saveDefaults(spec);
				return;
			}

			JsonObject root = JsonParser.parseReader(Files.newBufferedReader(path)).getAsJsonObject();

			for (ConfigValue<?> raw : spec.values.values()) {
				ConfigValue value = raw; // safe cast

				if (!root.has(value.key)) {
					value.value = value.defaultValue;
					continue;
				}

				value.value = value.type.fromJson(root.get(value.key));
			}

		} catch (Exception e) {
			saveDefaults(spec);
		}
	}

	public static void saveDefaults(JsonConfigSpec spec) {
		try {
			Path path = Path.of(spec.path);
			Files.createDirectories(path.getParent());
			JsonObject root = new JsonObject();

			for (ConfigValue<?> raw : spec.values.values()) {
				ConfigValue value = raw; // safe cast
				root.add(value.key, value.type.toJson(value.defaultValue));
			}

			Files.writeString(path, root.toString(),
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		} catch (Exception ignored) {}
	}

}
