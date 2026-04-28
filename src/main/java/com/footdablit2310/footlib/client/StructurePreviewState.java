package com.footdablit2310.footlib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class StructurePreviewState {

    private static List<BlockPos> relPositions;
    private static List<BlockState> states;
    private static BlockPos anchor;   // where server placed it (block you looked at)
    private static BlockPos offset;   // movement offset from anchor
    private static boolean full;

    private StructurePreviewState() {}

    public static void set(BlockPos anchorPos,
                           List<BlockPos> relativePositions,
                           List<BlockState> blockStates,
                           boolean fullMode) {
        anchor = anchorPos;
        relPositions = relativePositions;
        states = blockStates;
        offset = BlockPos.ZERO;
        full = fullMode;
    }

    public static void clear() {
        relPositions = null;
        states = null;
        anchor = null;
        offset = BlockPos.ZERO;
    }

    public static void nudge(int dx, int dy, int dz) {
        if (anchor == null) return;
        offset = offset.offset(dx, dy, dz);
    }

    public static void render(PoseStack pose) {
        if (relPositions == null || states == null || anchor == null) return;

        Minecraft mc = Minecraft.getInstance();

        BlockPos base = anchor.offset(offset);

        for (int i = 0; i < relPositions.size(); i++) {
            BlockPos rel = relPositions.get(i);
            BlockState state = states.get(i);

            BlockPos worldPos = base.offset(rel);

            if (full) {
                GhostBlockRenderer.renderFull(mc, worldPos, state, pose);
            } else {
                GhostBlockRenderer.renderOutline(mc, worldPos, pose);
            }
        }
    }
}
