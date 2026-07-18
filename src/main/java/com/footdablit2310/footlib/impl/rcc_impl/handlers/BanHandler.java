package com.footdablit2310.footlib.impl.rcc_impl.handlers;

import com.footdablit2310.footlib.api.common.rcc_api.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import net.minecraft.server.players.NameAndId;
import java.util.Date;

public class BanHandler {
    // Inject your mod’s logger here
    private static final Logger LOGGER = com.footdablit2310.footlib.FootLib.LOGGER;

    public static RCCResponse handle(RCCCommand cmd) {
        String playerName = cmd.getData().get("player");
        String reason = cmd.getData().get("reason");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Data responseData = new Data();
        responseData.put("player", playerName);
        responseData.put("reason", reason);

        if (server != null) {
            ServerPlayer player = server.getPlayerList().getPlayerByName(playerName);
            if (player == null) {
                // Warn instead of fabricating a profile
                LOGGER.warn("Attempted to ban player '{}', but they are not online and no profile could be resolved.", playerName);
                return new RCCResponse("error", "Player not found online", responseData);
            }

            GameProfile profile = player.getGameProfile();
            NameAndId nameAndIdObj=new NameAndId(profile.id(), profile.name());
            UserBanList banList = server.getPlayerList().getBans();
            UserBanListEntry entry = new UserBanListEntry(
                    nameAndIdObj,
                    new Date(),          // created
                    "RCC",               // source
                    null,                // expiry (null = permanent)
                    reason               // reason
            );
            banList.add(entry);

            player.connection.disconnect(Component.literal("Banned: " + reason));
            return new RCCResponse("success", "Player banned", responseData);
        }
        return new RCCResponse("error", "Server not available", responseData);
    }
}
