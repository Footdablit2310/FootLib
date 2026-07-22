package com.footdablit2310.footlib.impl.rcc_impl.handlers;

import com.footdablit2310.footlib.api.common.rcc_api.*;
import com.footdablit2310.footlib.FootLib;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

public class WhitelistHandler {
    public static final Logger LOGGER = FootLib.LOGGER;
    public static RCCResponse handle(RCCCommand cmd) {
        String playerName = cmd.getData().get("player");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Data responseData = new Data();
        responseData.put("player", playerName);
        if (server != null) {
            ServerPlayer player = server.getPlayerList().getPlayerByName(playerName);
            if (player != null) {
                GameProfile profile = player.getGameProfile();
                UserWhiteList whitelist = server.getPlayerList().getWhiteList();
                UserWhiteListEntry entry = new UserWhiteListEntry(profile);
                whitelist.add(entry);

                return new RCCResponse("success", "Player whitelisted", responseData);
            } else {
                LOGGER.warn("Player could not be found.");
                return new RCCResponse("warning", "Player not found", responseData);
            }
        }
        LOGGER.error("Server not available");
        return new RCCResponse("error", "Server not available", responseData);
    }
}
