package com.footdablit2310.footlib.api.common.json_config.events;

import com.footdablit2310.footlib.api.common.json_config.JsonConfigSpec;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FootLibConfigRegisterEvent extends Event {

	private final List<JsonConfigSpec> specs = new ArrayList<>();

	public void register(JsonConfigSpec spec) {
		specs.add(spec);
	}

	public void registerAll(JsonConfigSpec... specs) {
		Collections.addAll(this.specs, specs);
	}

	public void registerAll(Collection<JsonConfigSpec> specs) {
		this.specs.addAll(specs);
	}

	public List<JsonConfigSpec> getSpecs() {
		return specs;
	}
}
