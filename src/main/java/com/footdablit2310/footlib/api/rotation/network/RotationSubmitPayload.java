package com.footdablit2310.footlib.api.rotation.network;

import com.footdablit2310.footlib.api.rotation.RotationAPI;
import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import com.footdablit2310.footlib.api.rotation.item.RotationToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaterniond;

/** Client-submitted quaternion. All validation and tool checks happen server-side. */
public record RotationSubmitPayload(BlockPos pos, double x, double y, double z, double w)
        implements CustomPacketPayload {
    public static final Type<RotationSubmitPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("footlib", "rotation_submit"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RotationSubmitPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, RotationSubmitPayload::pos,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::x,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::y,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::z,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::w,
                    RotationSubmitPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(RotationSubmitPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (player.distanceToSqr(payload.pos().getX() + 0.5,
                    payload.pos().getY() + 0.5, payload.pos().getZ() + 0.5) > 64.0) return;
            BlockEntity blockEntity = player.level().getBlockEntity(payload.pos());
            if (!(blockEntity instanceof RotatableBlockEntity)) return;

            RotationToolItem tool = null;
            InteractionHand usedHand = null;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof RotationToolItem found) {
                    tool = found;
                    usedHand = hand;
                    break;
                }
            }
            if (tool == null) return;

            try {
                Quaterniond input = new Quaterniond(payload.x(), payload.y(), payload.z(), payload.w());
                if (RotationAPI.applyInput(blockEntity, input, tool.precisionDegrees())) {
                    player.getItemInHand(usedHand).hurtAndBreak(1, player, usedHand);
                    player.closeContainer();
                }
            } catch (IllegalArgumentException ignored) {
                // Invalid client input is rejected without changing the BlockEntity.
            }
        });
    }
}
