package com.footdablit2310.footlib.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class StructurePreviewRenderer {

    private static List<BlockPos> positions;
    private static List<BlockState> states;
    private static boolean full;

    public static void set(List<BlockPos> posList, List<BlockState> stateList, boolean fullMode) {
        positions = posList;
        states = stateList;
        full = fullMode;
    }

    public static void clear() {
        positions = null;
        states = null;
    }

    public static void render(PoseStack pose) {
        if (positions == null || states == null) return;

        Minecraft mc = Minecraft.getInstance();

        for (int i = 0; i < positions.size(); i++) {
            BlockPos pos = positions.get(i);
            BlockState state = states.get(i);

            if (full) {
                GhostBlockRenderer.renderFull(mc, pos, state, pose);
            } else {
                GhostBlockRenderer.renderOutline(mc, pos, pose);
            }
        }
    }
}
