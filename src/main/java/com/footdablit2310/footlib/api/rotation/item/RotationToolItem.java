package com.footdablit2310.footlib.api.rotation.item;

import com.footdablit2310.footlib.api.rotation.RotationAPI;
import com.footdablit2310.footlib.api.rotation.RotationBlockConfig;
import com.footdablit2310.footlib.api.rotation.RotationConfigManager;
import com.footdablit2310.footlib.api.rotation.RotatableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniond;

/**
 * Basic rotation tool: use normally for Y, crouch-use for X.
 * Rotation requests are applied only on the logical server.
 */
public class RotationToolItem extends Item {
    public RotationToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = context.getLevel().getBlockEntity(pos);
        if (!(blockEntity instanceof RotatableBlockEntity)) {
            return InteractionResult.PASS;
        }

        RotationBlockConfig config = RotationConfigManager.get(
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock())
        );
        if (config == null || !config.enabled()) {
            return InteractionResult.PASS;
        }

        boolean rotateX = context.getPlayer() != null && context.getPlayer().isCrouching();
        String axis = rotateX ? "x" : "y";
        if (!config.allowsAxis(axis)) {
            return InteractionResult.FAIL;
        }

        double radians = Math.toRadians(config.rotationStepDegrees());
        Quaterniond delta = switch (axis) {
            case "x" -> new Quaterniond().rotationX(radians);
            case "y" -> new Quaterniond().rotationY(radians);
            default -> new Quaterniond().rotationZ(radians);
        };

        return RotationAPI.rotate(blockEntity, delta)
                ? InteractionResult.CONSUME
                : InteractionResult.FAIL;
    }
}
