package com.footdablit2310.footlib.api.common.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.BlockHitResult;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class FootLibVisualizeStructureCommand {

    public static void register(RegisterCommandsEvent event) {

        event.getDispatcher().register(
            Commands.literal("visualize")
                .then(Commands.literal("structure")
                    .then(Commands.argument("id", ResourceLocationArgument.id())
                        .suggests((ctx, builder) -> {
                            Registry<Structure> reg =
                                ctx.getSource().getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);

                            for (ResourceLocation rl : reg.keySet()) {
                                builder.suggest(rl.toString());
                            }
                            return builder.buildFuture();
                        })
                        .then(Commands.argument("full", BoolArgumentType.bool())
                            .executes(ctx -> execute(
                                ctx.getSource(),
                                ResourceLocationArgument.getId(ctx, "id"),
                                BoolArgumentType.getBool(ctx, "full")
                            ))
                        )
                    )
                )
        );
    }

    private static int execute(CommandSourceStack src, ResourceLocation id, boolean full) {

        ServerPlayer player = src.getPlayer();
        if (player == null) {
            src.sendFailure(Component.literal("Player required"));
            return 0;
        }

        Registry<Structure> reg =
            src.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);

        Structure structure = reg.get(id);
        if (structure == null) {
            src.sendFailure(Component.literal("Unknown structure: " + id));
            return 0;
        }

        var hit = player.pick(20, 0, false);
        if (!(hit instanceof BlockHitResult bhr)) {
            src.sendFailure(Component.literal("Look at a block to place the structure origin"));
            return 0;
        }

        BlockPos origin = bhr.getBlockPos();

        src.sendSuccess(() ->
            Component.literal("Would preview structure '" + id + "' at " + origin + " (full=" + full + ")"),
            false
        );

        return 1;
    }
}
