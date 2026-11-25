package com.footdablit2310.footlib.api.rotation.item;

import com.footdablit2310.footlib.api.rotation.RotationConfigManager;
import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import com.footdablit2310.footlib.api.rotation.menu.RotationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Quaterniond;

/** Opens the quaternion input screen when used on an enabled rotatable BlockEntity. */
public final class RotationToolItem extends Item {
    private final double precisionDegrees;

    public RotationToolItem(Properties properties, double precisionDegrees) {
        super(properties);
        if (!Double.isFinite(precisionDegrees) || precisionDegrees <= 0.0 || precisionDegrees >= 360.0)
            throw new IllegalArgumentException("precisionDegrees must be in (0, 360)");
        this.precisionDegrees = precisionDegrees;
    }

    public double precisionDegrees() { return precisionDegrees; }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = context.getLevel().getBlockEntity(pos);
        if (!(blockEntity instanceof RotatableBlockEntity rotatable)) return InteractionResult.PASS;
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
        if (!(context.getPlayer() instanceof ServerPlayer player)) return InteractionResult.PASS;

        var settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return InteractionResult.PASS;
        InteractionHand hand = context.getHand();
        Quaterniond currentInput = rotatable.footlib$getRotationData().getLastInput();
        ResourceLocation toolId = BuiltInRegistries.ITEM.getKey(this);
        int axesMask = (settings.allowedAxes().contains("x") ? 1 : 0)
                | (settings.allowedAxes().contains("y") ? 2 : 0)
                | (settings.allowedAxes().contains("z") ? 4 : 0)
                | (settings.allowedAxes().contains("w") ? 8 : 0);

        player.openMenu(new SimpleMenuProvider(
                (id, inventory, user) -> new RotationMenu(id, inventory, pos, currentInput, hand, toolId,
                        settings.allowedAxes()),
                Component.translatable("gui.footlib.rotation_editor")
        ), buffer -> {
            buffer.writeBlockPos(pos);
            buffer.writeDouble(currentInput.x);
            buffer.writeDouble(currentInput.y);
            buffer.writeDouble(currentInput.z);
            buffer.writeDouble(currentInput.w);
            buffer.writeByte(hand.ordinal());
            buffer.writeResourceLocation(toolId);
            buffer.writeByte(axesMask);
        });
        return InteractionResult.CONSUME;
    }
}
