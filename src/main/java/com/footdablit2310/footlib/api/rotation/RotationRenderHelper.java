package com.footdablit2310.footlib.api.rotation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;

/** Rendering adapter; all stored rotation math stays double precision. */
public final class RotationRenderHelper {
    private RotationRenderHelper() {}

    /** Call after translating the PoseStack to the block origin. */
    public static boolean apply(PoseStack poseStack, BlockEntity blockEntity) {
        var rotation = RotationAPI.getRotation(blockEntity);
        if (rotation.isEmpty()) return false;
        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        Vector3d pivot = settings.pivot();
        apply(poseStack, rotation.get(), pivot);
        return true;
    }

    public static void apply(PoseStack poseStack, Quaterniond rotation, Vector3d pivot) {
        poseStack.translate(pivot.x, pivot.y, pivot.z);
        poseStack.mulPose(new Quaternionf((float) rotation.x, (float) rotation.y,
                (float) rotation.z, (float) rotation.w));
        poseStack.translate(-pivot.x, -pivot.y, -pivot.z);
    }
}
