package com.footdablit2310.footlib.api.common.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

import com.footdablit2310.footlib.api.common.multiblock.*;

public class FootLibVisualizeCommand {

    public static void register(net.neoforged.neoforge.event.RegisterCommandsEvent event) {

        event.getDispatcher().register(
            Commands.literal("visualize")
                .then(Commands.literal("multiblock")
                    .then(Commands.argument("id", StringArgumentType.string())
                        .then(Commands.argument("tier", IntegerArgumentType.integer(1))
                            .executes(ctx -> visualizeMultiblock(
                                ctx.getSource(),
                                StringArgumentType.getString(ctx, "id"),
                                IntegerArgumentType.getInteger(ctx, "tier")
                            ))
                        )
                    )
                )
                .then(Commands.literal("structure")
                    .then(Commands.argument("id", StringArgumentType.string())
                        .executes(ctx -> visualizeStructure(
                            ctx.getSource(),
                            StringArgumentType.getString(ctx, "id")
                        ))
                    )
                )
        );
    }

    // -------------------------------------------------------------------------
    // MULTIBLOCK VISUALIZATION
    // -------------------------------------------------------------------------
    private static int visualizeMultiblock(CommandSourceStack src, String id, int tier) {
        ServerLevel level = src.getLevel();
        BlockPos origin = BlockPos.containing(src.getPosition());

        MultiBlockDataFormat data = MultiBlockRegistry.get(id);
        if (data == null) {
            src.sendFailure(net.minecraft.network.chat.Component.literal("Unknown multiblock: " + id));
            return 0;
        }

        MultiBlockShape shape = data.shapeForTier(tier);
        if (shape == null) {
            src.sendFailure(net.minecraft.network.chat.Component.literal("Tier " + tier + " not found for " + id));
            return 0;
        }

        Map<BlockPos, Block> coords = MultiBlockHelpers.toCoords(shape);

        coords.forEach((pos, block) -> {
            BlockPos worldPos = origin.offset(pos);
            spawnPreviewParticle(level, worldPos);
        });

        src.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "Visualizing multiblock '" + id + "' tier " + tier), false);

        return 1;
    }

    // -------------------------------------------------------------------------
    // STRUCTURE VISUALIZATION (placeholder)
    // -------------------------------------------------------------------------
    private static int visualizeStructure(CommandSourceStack src, String id) {
        src.sendFailure(net.minecraft.network.chat.Component.literal(
                "Structure visualization not implemented yet: " + id));
        return 0;
    }

    // -------------------------------------------------------------------------
    // PARTICLE PREVIEW
    // -------------------------------------------------------------------------
    private static void spawnPreviewParticle(ServerLevel level, BlockPos pos) {
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.END_ROD,
                center.x, center.y, center.z,
                4, 0.1, 0.1, 0.1, 0.01
        );
    }
}
