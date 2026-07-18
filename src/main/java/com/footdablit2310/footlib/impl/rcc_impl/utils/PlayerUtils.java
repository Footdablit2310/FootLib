package com.footdablit2310.footlib.impl.rcc_impl.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
}
