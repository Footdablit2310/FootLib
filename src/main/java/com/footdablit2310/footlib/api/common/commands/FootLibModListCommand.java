package com.footdablit2310.footlib.api.common.commands;

import com.footdablit2310.footlib.api.common.basic.ModPresence;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class FootLibModListCommand {

    public static final String SIDE_CLIENT = "client";
    public static final String SIDE_SERVER = "server";

    private FootLibModListCommand() {}

    public static void register(RegisterCommandsEvent event) {

        event.getDispatcher().register(
            Commands.literal("modlist")
                .then(Commands.argument("side", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        builder.suggest(SIDE_SERVER);
                        builder.suggest(SIDE_CLIENT);
                        return builder.buildFuture();
                    })
                    .executes(ctx -> execute(
                        ctx.getSource(),
                        StringArgumentType.getString(ctx, "side")
                    ))
                )
        );
    }

    @SuppressWarnings("unused")
    private static int execute(CommandSourceStack src, String side) {

        boolean clientRequested = side.equalsIgnoreCase(SIDE_CLIENT);

        var ids = ModPresence.ReturnAllInstalledModIdsAsString();

        src.sendSuccess(() ->
                Component.literal("Installed mods (" + ids.size() + ") on " + side + ":"), false);

        for (String id : ids) {
            src.sendSuccess(() -> Component.literal(" - " + id), false);
        }

        return 1;
    }
}
