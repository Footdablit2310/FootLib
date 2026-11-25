package com.footdablit2310.footlib.api.rotation;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;

/**
 * Rendering adapter. Rotation remains double precision until the final
 * PoseStack boundary, where Minecraft's rendering quaternion is float-based.
 */
public final class RotationRenderHelper {
    private RotationRenderHelper() {
    }

    /**
     * Applies a quaternion around a block-local pivot expressed in block units.
     * Call after translating the pose stack to the block's world origin.
     */
    public static void apply(PoseStack poseStack, Quaterniond rotation, Vector3d pivot) {
        poseStack.translate(pivot.x, pivot.y, pivot.z);
        poseStack.mulPose(new Quaternionf(
                (float) rotation.x,
                (float) rotation.y,
                (float) rotation.z,
                (float) rotation.w
        ));
        poseStack.translate(-pivot.x, -pivot.y, -pivot.z);
    }
}
