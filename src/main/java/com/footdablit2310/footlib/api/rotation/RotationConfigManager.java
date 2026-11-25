package com.footdablit2310.footlib.api.rotation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Loads data-pack JSON at data/<namespace>/footlib_rotation/<block_path>.json.
 */
public final class RotationConfigManager implements PreparableReloadListener {
    private static final AtomicReference<Map<ResourceLocation, RotationBlockConfig>> CONFIGS =
            new AtomicReference<>(Map.of());

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new RotationConfigManager());
    }

    public static RotationBlockConfig get(ResourceLocation blockId) {
        return CONFIGS.get().get(blockId);
    }

    public static boolean isEnabled(ResourceLocation blockId) {
        RotationBlockConfig config = get(blockId);
        return config != null && config.enabled();
    }

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            ResourceManager resourceManager,
            ProfilerFiller preparationsProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ResourceLocation, RotationBlockConfig> loaded = new HashMap<>();
            Map<ResourceLocation, Resource> resources =
                    resourceManager.listResources("footlib_rotation", id -> id.getPath().endsWith(".json"));

            resources.forEach((fileId, resource) -> {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    ResourceLocation blockId = ResourceLocation.parse(requiredString(json, "block"));
                    boolean enabled = !json.has("enabled") || json.get("enabled").getAsBoolean();

                    JsonObject rotation = json.getAsJsonObject("rotation");
                    double step = rotation.has("rotation_step_degrees")
                            ? rotation.get("rotation_step_degrees").getAsDouble() : 90.0;
                    boolean smooth = !rotation.has("smooth") || rotation.get("smooth").getAsBoolean();
                    int ticks = rotation.has("interpolation_ticks")
                            ? rotation.get("interpolation_ticks").getAsInt() : 10;

                    Set<String> axes = Set.of("x", "y", "z");
                    if (rotation.has("allowed_axes")) {
                        var array = rotation.getAsJsonArray("allowed_axes");
                        var parsed = new java.util.HashSet<String>();
                        for (JsonElement axis : array) parsed.add(axis.getAsString().toLowerCase());
                        axes = Set.copyOf(parsed);
                    }

                    RotationBlockConfig config =
                            new RotationBlockConfig(blockId, enabled, step, axes, smooth, ticks);
                    if (loaded.put(blockId, config) != null) {
                        throw new IllegalArgumentException("Duplicate rotation config for " + blockId);
                    }
                } catch (Exception exception) {
                    throw new IllegalStateException("Invalid FootLib rotation config: " + fileId, exception);
                }
            });
            return Map.copyOf(loaded);
        }, backgroundExecutor).thenCompose(barrier::wait).thenAcceptAsync(CONFIGS::set, gameExecutor);
    }

    private static String requiredString(JsonObject json, String key) {
        if (!json.has(key) || !json.get(key).isJsonPrimitive()) {
            throw new IllegalArgumentException("Missing string field: " + key);
        }
        return json.get(key).getAsString();
    }
}
