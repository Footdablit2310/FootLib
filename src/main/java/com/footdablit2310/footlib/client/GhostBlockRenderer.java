package com.footdablit2310.footlib.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.client.model.data.ModelData;

public final class GhostBlockRenderer {

    private GhostBlockRenderer() {}

    public static void renderFull(Minecraft mc, BlockPos pos, BlockState state, PoseStack pose) {

        pose.pushPose();
        pose.translate(pos.getX(), pos.getY(), pos.getZ());

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        mc.getBlockRenderer().renderSingleBlock(
                state,
                pose,
                buffers,
                15728880,
                0,
                ModelData.EMPTY,
                RenderType.translucent()
        );

        pose.popPose();
    }

    public static void renderOutline(Minecraft mc, BlockPos pos, PoseStack pose) {

        var buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.lines());

        net.minecraft.client.renderer.LevelRenderer.renderLineBox(
                pose,
                buffer,
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1,
                0f, 1f, 1f, 1f
        );
    }
}
