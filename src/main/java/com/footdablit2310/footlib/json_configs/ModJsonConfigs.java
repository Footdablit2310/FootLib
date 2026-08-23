package com.footdablit2310.footlib.json_configs;

import com.footdablit2310.footlib.api.common.json_config.ConfigBuilder;
import com.footdablit2310.footlib.api.common.json_config.ConfigValue;
import com.footdablit2310.footlib.api.common.json_config.JsonConfigSpec;
import com.footdablit2310.footlib.api.common.json_config.Types;

import java.util.List;

public final class ModJsonConfigs {

	public static final JsonConfigSpec MAINTENANCE;

	static {
		ConfigBuilder cfg = new ConfigBuilder(new ConfigBuilder.Properties()
			.path("config/footlib/maintenance.json")
		);

		cfg.define(new ConfigValue<>(
			"validationSpeed",
			Types.STRING,
			"balanced"
		));

		cfg.define(new ConfigValue<>(
			"maintenanceEnabled",
			Types.BOOL,
			false
		));

		cfg.define(new ConfigValue<>(
			"allowedPlayers",
			Types.listOf(Types.UUID),
			List.of()
		));

		MAINTENANCE = cfg.build();
	}

	private ModJsonConfigs() {}
}
