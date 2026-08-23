package com.footdablit2310.footlib.config;

import com.footdablit2310.footlib.api.common.json_config.JsonConfigSpec;
import com.footdablit2310.footlib.api.common.json_config.events.FootLibConfigRegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class FootLibConfigBootstrap {

	@SubscribeEvent
	public static void onFootLibConfigRegister(FootLibConfigRegisterEvent event) {

		for (JsonConfigSpec spec : event.getSpecs()) {
			ConfigIO.load(spec);
			NeoForge.EVENT_BUS.register(new ConfigReloader(spec));
		}
	}

}
