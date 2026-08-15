package com.footdablit2310.footlib.item;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.menu.MultiblockValidatorItemMenu;
import com.footdablit2310.footlib.multiblock.FootLibMultiblockUtil;

import com.footdablit2310.footlib.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MultiBlockValidatorItem extends Item {

    public MultiBlockValidatorItem(Properties props) {
        super(props);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);
        Player player = ctx.getPlayer();

        if (be == null) {
            send(ctx, "No block entity found.");
            return InteractionResult.FAIL;
        }

        // STEP 1 — Find all multiblocks associated with this BE type
        List<ResourceLocation> ids = FootLibMultiblockUtil.findAllIds(be.getType());
        if (ids.isEmpty()) {
            send(ctx, "This block entity has no multiblock definitions.");
            return InteractionResult.FAIL;
        }

        // STEP 2 — Open selection menu (client-side)
        if (ctx.getPlayer() != null && level.isClientSide) {
            assert player != null;
            Objects.requireNonNull(player.getServer()).getPlayerList().getPlayerByName(player.getName().getString()).openMenu(
                    new SimpleMenuProvider(
                            (containerId, inv) -> new MultiblockValidatorItemMenu(containerId, inv),
                            Component.literal("Multiblock Validator Menu").setStyle(new Style())
                    )
            )
        }
        return InteractionResult.SUCCESS;
    }

    private void send(UseOnContext ctx, String msg) {
        if (ctx.getPlayer() != null) {
            ctx.getPlayer().displayClientMessage(Component.literal(msg), true);
        }
    }
}
