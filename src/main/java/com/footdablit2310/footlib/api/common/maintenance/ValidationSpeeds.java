package com.footdablit2310.footlib.api.common.maintenance;

import java.util.Map;

public final class ValidationSpeeds {

	public static final Map<String, Integer> SPEED_MAP = Map.of(
		"instant", 2,
		"fast", 5,
		"balanced", 10,
		"sloppy", 20,
		"performancePlus", 30
	);

	public static int resolve(String preset) {
		return SPEED_MAP.getOrDefault(preset, 10);
	}
}
