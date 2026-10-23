package com.footdablit2310.footlib.api.common.player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class FootPlayerUtil {

	private FootPlayerUtil() {}

	public static UUID resolveUUID(String name, MinecraftServer server) {
		// Online player?
		ServerPlayer online = server.getPlayerList().getPlayerByName(name);
		if (online != null) {
			return online.getUUID();
		}

		// Offline UUID (vanilla algorithm)
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name)
			.getBytes(StandardCharsets.UTF_8));
	}
}
