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

import java.io.InvalidObjectException;

public class WhitelistHandler {
    public static final Logger LOGGER = FootLib.LOGGER;
    public static RCCResponse handle(RCCCommand cmd) throws InvalidObjectException {
        if (!cmd.getSubcommand().isValid()) {
            throw new InvalidObjectException("SubCommand is Invalid");
        }
        String playerName = cmd.getData().get("player");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Data responseData = new Data();
        responseData.put("player", playerName);
        if (server != null) {
            ServerPlayer player = server.getPlayerList().getPlayerByName(playerName);
            if (player != null) {
                GameProfile profile = player.getGameProfile();
                UserWhiteList whitelist = server.getPlayerList().getWhiteList();
                if (cmd.getSubcommand().name().equalsIgnoreCase("ADD")) {
                    UserWhiteListEntry entry = new UserWhiteListEntry(profile);
                    whitelist.add(entry);
                    return new RCCResponse("success", "Player added to whitelist", responseData);
                } else if (cmd.getSubcommand().name().equalsIgnoreCase("REMOVE")) {
                    if (whitelist.isWhiteListed(profile)) {
                        whitelist.remove(profile);
                        return new RCCResponse("success", "Player removed from whitelist", responseData);
                    }
                    return new RCCResponse("info", "Player was not in whitelist", responseData);
                }
            } else {
                LOGGER.warn("Player could not be found.");
                return new RCCResponse("warning", "Player not found", responseData);
            }
        }
        LOGGER.error("Server not available");
        return new RCCResponse("error", "Server not available", responseData);
    }
}
