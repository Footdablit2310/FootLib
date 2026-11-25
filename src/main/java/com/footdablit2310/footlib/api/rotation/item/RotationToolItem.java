package com.footdablit2310.footlib.api.rotation.item;

import com.footdablit2310.footlib.api.rotation.RotationAPI;
import com.footdablit2310.footlib.api.rotation.RotationConfigManager;
import com.footdablit2310.footlib.api.rotation.RotationSettings;
import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniond;

/** Clicked face selects the axis; crouching reverses the rotation direction. */
public class RotationToolItem extends Item {
    public RotationToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;

        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = context.getLevel().getBlockEntity(pos);
        if (!(blockEntity instanceof RotatableBlockEntity)) return InteractionResult.PASS;

        RotationSettings settings = RotationConfigManager.getSettings(blockEntity.getBlockState());
        if (settings == null || !settings.enabled()) return InteractionResult.PASS;

        net.minecraft.core.Direction face = context.getClickedFace();
        String axis = face.getAxis().getName();
        boolean reverse = face.getAxisDirection() == net.minecraft.core.Direction.AxisDirection.NEGATIVE;
        if (context.getPlayer() != null && context.getPlayer().isCrouching()) reverse = !reverse;
        if (!settings.allowsAxis(axis)) return InteractionResult.FAIL;

        double degrees = reverse ? -settings.rotationStepDegrees() : settings.rotationStepDegrees();
        double radians = Math.toRadians(degrees);
        Quaterniond delta = switch (axis) {
            case "x" -> new Quaterniond().rotationX(radians);
            case "y" -> new Quaterniond().rotationY(radians);
            default -> new Quaterniond().rotationZ(radians);
        };

        return RotationAPI.rotate(blockEntity, delta)
                ? InteractionResult.CONSUME : InteractionResult.FAIL;
    }
}
