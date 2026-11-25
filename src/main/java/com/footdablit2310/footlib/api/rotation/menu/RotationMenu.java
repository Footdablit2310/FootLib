package com.footdablit2310.footlib.api.rotation.menu;

import com.footdablit2310.footlib.api.rotation.item.RotationItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;
import org.joml.Quaterniond;

/** Data-only menu used to synchronize the quaternion editor's target block. */
public final class RotationMenu extends AbstractContainerMenu {
    private final BlockPos blockPos;
    private final Quaterniond initialInput;

    public RotationMenu(int id, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(id, inventory, data.readBlockPos(),
                new Quaterniond(data.readDouble(), data.readDouble(), data.readDouble(), data.readDouble()));
    }

    public RotationMenu(int id, Inventory inventory, BlockPos blockPos, Quaterniond initialInput) {
        super(RotationItems.ROTATION_MENU.get(), id);
        this.blockPos = blockPos.immutable();
        this.initialInput = new Quaterniond(initialInput);
    }

    public BlockPos blockPos() { return blockPos; }
    public Quaterniond initialInput() { return new Quaterniond(initialInput); }

    @Override
    public boolean stillValid(Player player) {
        return player.level().isLoaded(blockPos)
                && player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) <= 64.0;
    }
}
