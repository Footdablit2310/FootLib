package com.footdablit2310.footlib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;


import java.util.List;

public final class StructurePreviewState {

    private StructurePreviewState() {}

    private static List<BlockPos> relPositions;
    private static List<BlockState> states;
    private static BlockPos anchor;   // where server placed it (block you looked at)
    private static BlockPos offset;   // movement offset from anchor
    private static boolean full;

    public static BlockPos min;
    public static BlockPos max;

    private static void computeBounds() {
        if (relPositions == null || relPositions.isEmpty()) {
            min = max = null;
            return;
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (BlockPos p : relPositions) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            minZ = Math.min(minZ, p.getZ());

            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
            maxZ = Math.max(maxZ, p.getZ());
        }

        min = new BlockPos(minX, minY, minZ);
        max = new BlockPos(maxX, maxY, maxZ);
    }

    private static void renderBoundingBox(Minecraft mc, PoseStack pose, BlockPos base) {
    if (min == null || max == null) return;

    double x1 = base.getX() + min.getX();
    double y1 = base.getY() + min.getY();
    double z1 = base.getZ() + min.getZ();

    double x2 = base.getX() + max.getX() + 1;
    double y2 = base.getY() + max.getY() + 1;
    double z2 = base.getZ() + max.getZ() + 1;

    VertexConsumer buffer = mc.renderBuffers()
            .bufferSource()
            .getBuffer(RenderType.lines());

    pose.pushPose();
    RenderSystem.disableDepthTest();

    LevelRenderer.renderLineBox(
            pose,
            buffer,
            x1, y1, z1,
            x2, y2, z2,
            1f, 0.5f, 0f, 1f
    );

    RenderSystem.enableDepthTest();
    pose.popPose();
}



    public static void set(BlockPos anchorPos,
                           List<BlockPos> relativePositions,
                           List<BlockState> blockStates,
                           boolean fullMode) {
        anchor = anchorPos;
        relPositions = relativePositions;
        states = blockStates;
        offset = BlockPos.ZERO;
        full = fullMode;
        computeBounds();
    }

    public static void clear() {
        relPositions = null;
        states = null;
        anchor = null;
        offset = BlockPos.ZERO;
    }

    public static void nudge(int dx, int dy, int dz) {
    if (offset == null) offset = BlockPos.ZERO;
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
        renderBoundingBox(mc, pose, base);
    }

}
