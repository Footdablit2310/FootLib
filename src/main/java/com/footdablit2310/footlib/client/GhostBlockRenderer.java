package com.footdablit2310.footlib.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.client.model.data.ModelData;

public final class GhostBlockRenderer {

    private GhostBlockRenderer() {}

    /**
     * Render a full translucent ghost block.
     */
    public static void renderFull(Minecraft mc, BlockPos pos, BlockState state, PoseStack pose) {

        pose.pushPose();

        // Convert world position → render-relative position
        var cam = mc.gameRenderer.getMainCamera();
        double camX = cam.getPosition().x;
        double camY = cam.getPosition().y;
        double camZ = cam.getPosition().z;

        pose.translate(
                pos.getX() - camX,
                pos.getY() - camY,
                pos.getZ() - camZ
        );

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        mc.getBlockRenderer().renderSingleBlock(
                state,
                pose,
                buffers,
                15728880, // full brightness
                0,
                ModelData.EMPTY,
                RenderType.translucent()
        );

        pose.popPose();
    }

    /**
     * Render an outline-only ghost block.
     */
    public static void renderOutline(Minecraft mc, BlockPos pos, PoseStack pose) {

        pose.pushPose();

        // Convert world position → render-relative position
        var cam = mc.gameRenderer.getMainCamera();
        double camX = cam.getPosition().x;
        double camY = cam.getPosition().y;
        double camZ = cam.getPosition().z;

        pose.translate(
                pos.getX() - camX,
                pos.getY() - camY,
                pos.getZ() - camZ
        );

        var buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                pose,
                buffer,
                0, 0, 0,
                1, 1, 1,
                0f, 1f, 1f, 1f // cyan outline
        );

        pose.popPose();
    }
}
