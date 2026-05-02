package com.footdablit2310.footlib.network;

import com.footdablit2310.footlib.api.common.visualize.FootLibVisualizationAPI;
import com.footdablit2310.footlib.client.StructurePreviewRenderer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

public record StructurePreviewPacket(
        String id,
        int tier,
        BlockPos origin,
        Direction facing
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("footlib", "structure_preview");

    public static final Type<StructurePreviewPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, StructurePreviewPacket> STREAM_CODEC =
        new StreamCodec<RegistryFriendlyByteBuf, StructurePreviewPacket>() {
            @Override
            public StructurePreviewPacket decode(RegistryFriendlyByteBuf buf) {
                String id = buf.readUtf();
                int tier = buf.readInt();
                BlockPos origin = buf.readBlockPos();
                Direction facing = Direction.from3DDataValue(buf.readInt());
                return new StructurePreviewPacket(id, tier, origin, facing);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, StructurePreviewPacket pkt) {
                buf.writeUtf(pkt.id());
                buf.writeInt(pkt.tier());
                buf.writeBlockPos(pkt.origin());
                buf.writeInt(pkt.facing().get3DDataValue());
            }
        };

    @Override
    public Type<StructurePreviewPacket> type() {
        return TYPE;
    }

    public static void handleClient(StructurePreviewPacket pkt) {
        var def = FootLibVisualizationAPI.get(pkt.id(), pkt.tier());
        if (def != null && Minecraft.getInstance().level != null) {
            StructurePreviewRenderer.show(def, pkt.origin(), pkt.facing());
        }
    }
}
