package com.footdablit2310.footlib.config;

import com.footdablit2310.footlib.api.common.json_config.JsonConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public final class ConfigReloader {

	private final JsonConfigSpec spec;
	private FileTime lastModified;
	private long lastSize;

	public ConfigReloader(JsonConfigSpec spec) {
		this.spec = spec;
	}

	@SubscribeEvent
	public void onTick(ServerTickEvent.Post event) {
		if (event.getServer().getTickCount() % 20 != 0) return;

		Path path = Path.of(spec.path);

		try {
			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

			FileTime modified = attrs.lastModifiedTime();
			long size = attrs.size();

			boolean changed = (lastModified == null || !modified.equals(lastModified))
				|| (lastSize != size);

			if (!changed) return;

			lastModified = modified;
			lastSize = size;

			ConfigIO.load(spec);

		} catch (Exception ignored) {}
	}
}
