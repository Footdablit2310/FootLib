package com.footdablit2310.footlib.api.common.visualize;

import com.footdablit2310.footlib.api.common.visualize.data.StructureDefinition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public final class FootLibVisualizationAPI {

    private static final Map<String, Map<Integer, StructureDefinition>> MULTIBLOCKS = new HashMap<>();
    private static final Map<String, Integer> DEFAULT_TIERS = new HashMap<>();

    // Implementation-side hook
    public interface PreviewSender {
        void send(ServerPlayer player, String id, int tier, BlockPos origin, Direction facing);
    }

    private static PreviewSender SENDER = (player, id, tier, origin, facing) -> {};

    private FootLibVisualizationAPI() {}

    public static void registerMultiblockTier(String id, int tier, StructureDefinition def) {
        MULTIBLOCKS.computeIfAbsent(id, k -> new HashMap<>()).put(tier, def);
        DEFAULT_TIERS.putIfAbsent(id, tier);
    }

    public static StructureDefinition get(String id, int tier) {
        var tiers = MULTIBLOCKS.get(id);
        if (tiers == null) return null;
        return tiers.get(tier);
    }

    public static int getDefaultTier(String id) {
        return DEFAULT_TIERS.getOrDefault(id, 1);
    }

    public static Iterable<String> getAllIds() {
        return MULTIBLOCKS.keySet();
    }

    public static void setPreviewSender(PreviewSender sender) {
        SENDER = sender;
    }

    public static void requestPreview(ServerPlayer player, String id, int tier, BlockPos origin, Direction facing) {
        SENDER.send(player, id, tier, origin, facing);
    }
}
