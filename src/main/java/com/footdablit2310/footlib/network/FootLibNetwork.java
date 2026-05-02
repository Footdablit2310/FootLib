package com.footdablit2310.footlib.network;

import com.footdablit2310.footlib.api.common.visualize.FootLibVisualizationAPI;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class FootLibNetwork {

    private FootLibNetwork() {}

    public static void register(RegisterPayloadHandlersEvent event) {

        // Register client-bound payload
        event.registrar("1")
                .playToClient(
                        StructurePreviewPacket.TYPE,
                        StructurePreviewPacket.STREAM_CODEC,
                        (pkt, ctx) -> ctx.enqueueWork(() -> StructurePreviewPacket.handleClient(pkt))
                );

        // Bridge API → network
        FootLibVisualizationAPI.setPreviewSender(
                (ServerPlayer player, String id, int tier,
                 net.minecraft.core.BlockPos origin,
                 net.minecraft.core.Direction facing) ->
                        player.connection.send(new StructurePreviewPacket(id, tier, origin, facing))
        );
    }
}
