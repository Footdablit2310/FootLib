package com.footdablit2310.footlib.impl.rcc_impl.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class PlayerUtils {
    public static ServerPlayer getPlayerByName(String name) {
        // Get the current running server
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return null; // server not running
        }
        return server.getPlayerList().getPlayerByName(name);
    }
    public static GameProfile getPlayerProfileByName(String name) {
        ServerPlayer player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        return player.getGameProfile();
    }
    public static NameAndId nameAndIdFromGameProfile(GameProfile gameProfile) {
        return new NameAndId(gameProfile.id(), gameProfile.name());
    }
    public static NameAndId nameAndIdFromName(String name) {
        GameProfile gameProfile =getPlayerProfileByName(name);
        assert gameProfile != null;
        return new NameAndId(gameProfile.id(), gameProfile.name());
    }
    public static NameAndId nameAndIdFromServerPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.nameAndId();
    }
}