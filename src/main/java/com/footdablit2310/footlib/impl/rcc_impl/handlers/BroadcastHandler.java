package com.footdablit2310.footlib.impl.rcc_impl.handlers;

import com.footdablit2310.footlib.api.common.rcc_api.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.io.InvalidObjectException;

public class BroadcastHandler {
    public static RCCResponse handle(RCCCommand cmd) throws InvalidObjectException {
        if (!cmd.getSubcommand().isValid()) {
            throw new InvalidObjectException("SubCommand is Invalid");
        }
        String message = cmd.getData().get("message");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Data responseData = new Data();
        responseData.put("message", message);

        if (server != null) {
            server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
            return new RCCResponse("success", "Message broadcasted", responseData);
        }
        return new RCCResponse("error", "Server not available", responseData);
    }
}
