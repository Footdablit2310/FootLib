package com.footdablit2310.footlib.api.rotation.network;

import com.footdablit2310.footlib.api.rotation.RotationAPI;
import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import com.footdablit2310.footlib.api.rotation.item.RotationToolItem;
import com.footdablit2310.footlib.api.rotation.menu.RotationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Quaterniond;

/** Client-submitted quaternion; the server validates menu, tool, settings and values. */
public record RotationSubmitPayload(BlockPos pos, double x, double y, double z, double w)
        implements CustomPacketPayload {
    public static final Type<RotationSubmitPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("footlib", "rotation_submit"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RotationSubmitPayload> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, RotationSubmitPayload::pos,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::x,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::y,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::z,
                    ByteBufCodecs.DOUBLE, RotationSubmitPayload::w,
                    RotationSubmitPayload::new);

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(RotationSubmitPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)
                    || !(player.containerMenu instanceof RotationMenu menu)
                    || !menu.blockPos().equals(payload.pos())
                    || !menu.stillValid(player)) return;

            ItemStack stack = player.getItemInHand(menu.toolHand());
            if (stack.isEmpty() || !net.minecraft.core.registries.BuiltInRegistries.ITEM
                    .getKey(stack.getItem()).equals(menu.toolItemId())
                    || !(stack.getItem() instanceof RotationToolItem tool)) return;

            if (!(player.level().getBlockEntity(payload.pos()) instanceof RotatableBlockEntity rotatable)) return;
            try {
                Quaterniond input = new Quaterniond(payload.x(), payload.y(), payload.z(), payload.w());
                if (RotationAPI.applyInput((net.minecraft.world.level.block.entity.BlockEntity) rotatable,
                        input, tool.precisionDegrees())) {
                    stack.hurtAndBreak(1, player, menu.toolHand());
                    player.closeContainer();
                }
            } catch (IllegalArgumentException ignored) {
                // Reject malformed input without mutating the BlockEntity.
            }
        });
    }
}
