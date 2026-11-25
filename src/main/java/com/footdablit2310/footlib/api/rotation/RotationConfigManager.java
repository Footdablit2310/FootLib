package com.footdablit2310.footlib.api.rotation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/** Loads data/namespace/footlib_rotation/block_path.json resources. */
public final class RotationConfigManager implements PreparableReloadListener {
    private static final AtomicReference<Map<ResourceLocation, RotationBlockConfig>> CONFIGS =
            new AtomicReference<>(Map.of());

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new RotationConfigManager());
    }

    public static RotationBlockConfig get(ResourceLocation blockId) {
        return CONFIGS.get().get(blockId);
    }

    public static RotationSettings getSettings(BlockState state) {
        ResourceLocation blockId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
        RotationBlockConfig config = get(blockId);
        return config == null ? null : config.resolve(state);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager resourceManager,
            ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
            Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ResourceLocation, RotationBlockConfig> loaded = new HashMap<>();
            Map<ResourceLocation, Resource> resources =
                    resourceManager.listResources("footlib_rotation", id -> id.getPath().endsWith(".json"));

            resources.forEach((fileId, resource) -> {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    ResourceLocation blockId = blockIdFromResource(fileId);
                    RotationSettings defaults = parseSettings(requiredObject(json, "default"), null);
                    Map<Map<String, String>, RotationSettingsOverride> overrides = new HashMap<>();

                    if (json.has("blockstates")) {
                        JsonObject states = requiredObject(json, "blockstates");
                        for (Map.Entry<String, JsonElement> entry : states.entrySet()) {
                            Map<String, String> matcher = parseStateMatcher(entry.getKey());
                            RotationSettingsOverride settings = parseOverride(entry.getValue().getAsJsonObject());
                            if (overrides.put(matcher, settings) != null) {
                                throw new IllegalArgumentException("Duplicate blockstate matcher: " + entry.getKey());
                            }
                        }
                    }

                    if (!net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(blockId)) {
                        throw new IllegalArgumentException("Unknown block registry ID: " + blockId);
                    }
                    RotationBlockConfig config = new RotationBlockConfig(defaults, overrides);
                    var possibleStates = net.minecraft.core.registries.BuiltInRegistries.BLOCK
                            .get(blockId).getStateDefinition().getPossibleStates();
                    for (Map<String, String> matcher : overrides.keySet()) {
                        if (!config.matchesAnyState(possibleStates, matcher)) {
                            throw new IllegalArgumentException("Blockstate matcher does not match any state of "
                                    + blockId + ": " + matcher);
                        }
                    }
                    for (BlockState possibleState : possibleStates) config.resolve(possibleState);
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

    private static ResourceLocation blockIdFromResource(ResourceLocation fileId) {
        String prefix = "footlib_rotation/";
        String path = fileId.getPath();
        if (!path.startsWith(prefix) || !path.endsWith(".json")) {
            throw new IllegalArgumentException("Unexpected rotation config path: " + fileId);
        }
        return ResourceLocation.fromNamespaceAndPath(fileId.getNamespace(),
                path.substring(prefix.length(), path.length() - ".json".length()));
    }

    private static RotationSettingsOverride parseOverride(JsonObject json) {
        Boolean enabled = json.has("enabled") ? json.get("enabled").getAsBoolean() : null;
        Quaterniond rotation = json.has("rotation") ? parseQuaternion(json.getAsJsonArray("rotation")) : null;
        Vector3d pivot = json.has("pivot") ? parseVector(json.getAsJsonArray("pivot")) : null;
        Boolean smooth = json.has("smooth") ? json.get("smooth").getAsBoolean() : null;
        Integer ticks = json.has("interpolation_ticks") ? json.get("interpolation_ticks").getAsInt() : null;
        Double step = json.has("rotation_step_degrees") ? finite(json.get("rotation_step_degrees")) : null;
        Set<String> axes = json.has("allowed_axes") ? parseAxes(json.getAsJsonArray("allowed_axes")) : null;
        return new RotationSettingsOverride(enabled, rotation, pivot, smooth, ticks, step, axes);
    }

    private static RotationSettings parseSettings(JsonObject json, RotationSettings inherited) {
        boolean enabled = json.has("enabled") ? json.get("enabled").getAsBoolean()
                : inherited == null || inherited.enabled();
        Quaterniond rotation = json.has("rotation") ? parseQuaternion(json.getAsJsonArray("rotation"))
                : inherited == null ? new Quaterniond() : inherited.rotation();
        Vector3d pivot = json.has("pivot") ? parseVector(json.getAsJsonArray("pivot"))
                : inherited == null ? new Vector3d(0.5, 0.5, 0.5) : inherited.pivot();
        boolean smooth = json.has("smooth") ? json.get("smooth").getAsBoolean()
                : inherited == null || inherited.smooth();
        int ticks = json.has("interpolation_ticks") ? json.get("interpolation_ticks").getAsInt()
                : inherited == null ? 10 : inherited.interpolationTicks();
        double step = json.has("rotation_step_degrees") ? finite(json.get("rotation_step_degrees"))
                : inherited == null ? 90.0 : inherited.rotationStepDegrees();
        Set<String> axes = json.has("allowed_axes") ? parseAxes(json.getAsJsonArray("allowed_axes"))
                : inherited == null ? Set.of("x", "y", "z", "w") : inherited.allowedAxes();
        return new RotationSettings(enabled, rotation, pivot, smooth, ticks, step, axes);
    }

    private static Quaterniond parseQuaternion(JsonArray array) {
        if (array.size() != 4) throw new IllegalArgumentException("rotation must contain [x, y, z, w]");
        Quaterniond q = new Quaterniond(finite(array.get(0)), finite(array.get(1)),
                finite(array.get(2)), finite(array.get(3)));
        double lengthSquared = q.lengthSquared();
        if (lengthSquared < 1.0E-24 || !Double.isFinite(lengthSquared))
            throw new IllegalArgumentException("rotation quaternion must be finite and non-zero");
        return q.normalize();
    }

    private static Vector3d parseVector(JsonArray array) {
        if (array.size() != 3) throw new IllegalArgumentException("pivot must contain [x, y, z]");
        return new Vector3d(finite(array.get(0)), finite(array.get(1)), finite(array.get(2)));
    }

    private static double finite(JsonElement element) {
        double value = element.getAsDouble();
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Numeric values must be finite");
        return value;
    }

    private static Set<String> parseAxes(JsonArray array) {
        Set<String> axes = new HashSet<>();
        for (JsonElement element : array) {
            String axis = element.getAsString().toLowerCase(Locale.ROOT);
            if (!Set.of("x", "y", "z", "w").contains(axis))
                throw new IllegalArgumentException("allowed_axes entries must be x, y, z, or w");
            axes.add(axis);
        }
        if (axes.isEmpty()) throw new IllegalArgumentException("allowed_axes cannot be empty");
        return Set.copyOf(axes);
    }

    private static Map<String, String> parseStateMatcher(String text) {
        Map<String, String> matcher = new HashMap<>();
        for (String part : text.split(",")) {
            String[] pair = part.trim().split("=", 2);
            if (pair.length != 2 || pair[0].isBlank() || pair[1].isBlank())
                throw new IllegalArgumentException("Invalid blockstate matcher: " + text);
            String property = pair[0].trim().toLowerCase(Locale.ROOT);
            String value = pair[1].trim().toLowerCase(Locale.ROOT);
            if (matcher.put(property, value) != null)
                throw new IllegalArgumentException("Duplicate property in blockstate matcher: " + text);
        }
        return Map.copyOf(matcher);
    }

    private static JsonObject requiredObject(JsonObject json, String key) {
        if (!json.has(key) || !json.get(key).isJsonObject())
            throw new IllegalArgumentException("Missing object field: " + key);
        return json.getAsJsonObject(key);
    }
}
