package com.footdablit2310.footlib.api.rotation.menu;

import com.footdablit2310.footlib.api.rotation.item.RotationItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.joml.Quaterniond;

import java.util.Set;

/** Data-only menu bound to the exact tool and target used to open it. */
public final class RotationMenu extends AbstractContainerMenu {
    private final BlockPos blockPos;
    private final Quaterniond initialInput;
    private final InteractionHand toolHand;
    private final ResourceLocation toolItemId;
    private final Set<String> allowedAxes;

    public RotationMenu(int id, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(id, inventory, data.readBlockPos(),
                new Quaterniond(data.readDouble(), data.readDouble(), data.readDouble(), data.readDouble()),
                InteractionHand.values()[Byte.toUnsignedInt(data.readByte())],
                data.readResourceLocation(), readAxes(data.readUnsignedByte()));
    }

    public RotationMenu(int id, Inventory inventory, BlockPos pos, Quaterniond input,
            InteractionHand hand, ResourceLocation toolItemId, Set<String> axes) {
        super(RotationItems.ROTATION_MENU.get(), id);
        this.blockPos = pos.immutable();
        this.initialInput = new Quaterniond(input);
        this.toolHand = hand;
        this.toolItemId = toolItemId;
        this.allowedAxes = Set.copyOf(axes);
    }

    private static Set<String> readAxes(int mask) {
        java.util.Set<String> axes = new java.util.HashSet<>();
        if ((mask & 1) != 0) axes.add("x");
        if ((mask & 2) != 0) axes.add("y");
        if ((mask & 4) != 0) axes.add("z");
        if ((mask & 8) != 0) axes.add("w");
        return Set.copyOf(axes);
    }

    public BlockPos blockPos() { return blockPos; }
    public Quaterniond initialInput() { return new Quaterniond(initialInput); }
    public InteractionHand toolHand() { return toolHand; }
    public ResourceLocation toolItemId() { return toolItemId; }
    public Set<String> allowedAxes() { return allowedAxes; }

    @Override
    public boolean stillValid(Player player) {
        if (!player.level().isLoaded(blockPos)
                || player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) > 64.0)
            return false;
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(
                player.getItemInHand(toolHand).getItem()).equals(toolItemId);
    }
}
