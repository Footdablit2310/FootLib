package com.footdablit2310.footlib.item;

import com.footdablit2310.footlib.multiblock.MultiblockRegistry;
import com.footdablit2310.footlib.multiblock.ValidationResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class MultiblockCreatorItem extends Item {
    public MultiblockCreatorItem(Properties props) {
        super(props);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        var level = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var player = ctx.getPlayer();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // Already formed? Break it.
        if (MultiblockRegistry.isFormed(level, pos)) {
            MultiblockRegistry.breakMultiblock(level, pos);
            if (player != null) {
                player.sendSystemMessage(Component.translatable("multiblock.footlib.broken"));
            }
            return InteractionResult.SUCCESS;
        }

        // Validate via registry — pass BlockPos only, registry resolves BE + type
        ValidationResult result = MultiblockRegistry.validate(level, pos);

        if (player != null) {
            player.sendSystemMessage(result.message());
        }

        if (!result.success()) {
            return InteractionResult.FAIL;
        }

        // Form via registry
        MultiblockRegistry.form(level, pos);
        return InteractionResult.SUCCESS;
    }
}