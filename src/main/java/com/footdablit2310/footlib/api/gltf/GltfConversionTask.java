package com.footdablit2310.footlib.api.gltf;

import com.footdablit2310.footlib.api.gltf.builders.GltfConfigRegistry;
import com.footdablit2310.footlib.api.gltf.builders.GltfTargetType;
import net.minecraft.resources.ResourceLocation;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class GltfConversionTask extends DefaultTask {

	@TaskAction
	public void runGltfConversion() throws Exception {

		// 1. Find entrypoint class
		Class<?> entrypoint = findEntrypointClass();

		// 2. Call register()
		entrypoint.getMethod("register").invoke(null);

		// 3. Get all configs
		var configs = GltfConfigRegistry.getAllConfigs();

		// 4. Convert each GLTF file
		for (var typeEntry : configs.entrySet()) {
			GltfTargetType type = typeEntry.getKey();
			var map = typeEntry.getValue();

			for (var cfg : map.entrySet()) {
				ResourceLocation targetId = cfg.getKey();
				ResourceLocation gltfId   = cfg.getValue();

				convertGltf(gltfId, targetId, type);
			}
		}
	}

	private Class<?> findEntrypointClass() throws Exception {
		// Classpath scanning logic (ClassGraph, Reflections, etc.)
		// Find class annotated with @GltfEntrypoint
		throw new UnsupportedOperationException("Implement scanning");
	}

	private void convertGltf(ResourceLocation gltfId, ResourceLocation targetId, GltfTargetType type) {
		// Your GLTF → FootLib runtime converter
	}
}
