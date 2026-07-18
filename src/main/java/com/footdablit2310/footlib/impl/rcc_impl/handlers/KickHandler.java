package com.footdablit2310.footlib.impl.rcc_impl.handlers;

import com.footdablit2310.footlib.impl.rcc_impl.utils.*;
import com.footdablit2310.footlib.api.common.rcc_api.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

public class KickHandler {
    public static RCCResponse handle(RCCCommand cmd) {
        String playerName = cmd.getData().get("player");
        String reason = cmd.getData().get("reason");

        ServerPlayer player = PlayerUtils.getPlayerByName(playerName);
        Data responseData = new Data();
        responseData.put("player", playerName);
        responseData.put("reason", reason);

        if (player != null) {
            // Directly disconnect the player
            player.connection.disconnect(Component.literal(reason != null ? reason : "Kicked by RCC"));
            return new RCCResponse("success", "Player disconnected", responseData);
        } else {
            return new RCCResponse("error", "Player not found", responseData);
        }
    }
}
