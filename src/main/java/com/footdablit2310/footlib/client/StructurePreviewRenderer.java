package com.footdablit2310.footlib.client;

import com.footdablit2310.footlib.api.common.visualize.data.StructureDefinition;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Rotation;

public final class StructurePreviewRenderer {

    private static StructureDefinition def;
    private static BlockPos origin;
    private static Direction facing;

    public static void show(StructureDefinition d, BlockPos o, Direction f) {
        def = d;
        origin = o;
        facing = f;
    }

    @Deprecated // Uses Depracated Neoforge Code
    public static void render(PoseStack pose, float partialTick) {
        if (def == null) return;

        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null) return;

        var cam = mc.gameRenderer.getMainCamera();
        var camPos = cam.getPosition();

        pose.pushPose();
        pose.translate(-camPos.x, -camPos.y, -camPos.z);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        var dispatcher = mc.getBlockRenderer();

        // Convert facing → Rotation enum
        Rotation rotation = switch (facing) {
            case NORTH -> Rotation.NONE;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST  -> Rotation.COUNTERCLOCKWISE_90;
            case EAST  -> Rotation.CLOCKWISE_90;
            default    -> Rotation.NONE;
        };

        for (var entry : def.getLayout().entrySet()) {

            BlockPos rel = entry.getKey();
            Block block = entry.getValue();
            BlockState state = block.defaultBlockState();

            // rotate relative position
            BlockPos rotated = rel.rotate(rotation);

            // world position
            BlockPos worldPos = origin.offset(rotated);

            pose.pushPose();
            pose.translate(worldPos.getX(), worldPos.getY(), worldPos.getZ());

            dispatcher.renderSingleBlock(
                    state,
                    pose,
                    buffer,
                    15728880,   // full brightness
                    0           // overlay
            );

            pose.popPose();
        }

        pose.popPose();
    }
}
