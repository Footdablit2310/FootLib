package com.footdablit2310.footlib.api.common.commands;

import com.footdablit2310.footlib.api.common.visualize.FootLibVisualizationAPI;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public final class VisualizeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
            Commands.literal("visualize")
                .then(Commands.literal("multiblock")
                    .then(Commands.argument("id", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            FootLibVisualizationAPI.getAllIds().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .then(Commands.argument("tier", StringArgumentType.string())
                            .suggests((ctx, builder) -> {
                                builder.suggest("none");
                                builder.suggest("1");
                                builder.suggest("2");
                                builder.suggest("3");
                                builder.suggest("4");
                                builder.suggest("5");
                                builder.suggest("6");
                                return builder.buildFuture();
                            })
                            .executes(ctx -> execute(
                                ctx.getSource(),
                                StringArgumentType.getString(ctx, "id"),
                                StringArgumentType.getString(ctx, "tier")
                            ))
                        )
                    )
                )
        );
    }

    private static int execute(CommandSourceStack source, String id, String tierStr) {

        if (!(source.getEntity() instanceof ServerPlayer player)) return 0;

        int tier = tierStr.equalsIgnoreCase("none")
                ? FootLibVisualizationAPI.getDefaultTier(id)
                : Integer.parseInt(tierStr);

        var def = FootLibVisualizationAPI.get(id, tier);
        if (def == null) {
            source.sendFailure(Component.literal("Unknown multiblock tier: " + id + " tier " + tier));
            return 0;
        }

        BlockPos origin = player.blockPosition().below();
        Direction facing = player.getDirection();

        FootLibVisualizationAPI.requestPreview(player, id, tier, origin, facing);

        // ✔ FIXED: use Supplier<Component>
        source.sendSuccess(() -> Component.literal("Previewing " + id + " tier " + tier), false);
        return 1;
    }


}
