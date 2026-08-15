package com.footdablit2310.footlib.menu;

import com.footdablit2310.footlib.registry.ModRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MultiblockValidatorItemMenu extends AbstractContainerMenu {
    public MultiblockValidatorItemMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
        super(ModRegistry.MULTIBLOCK_VALIDATOR_ITEM_MENU.get(), containerId);
    }

    private boolean stillValidBoolean = true;
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();

            // --- Data inventory result slot (0) ---
            if (quickMovedSlotIndex == 0) {
                if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
            }

            // --- Player inventory or hotbar (5–40) ---
            else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {

                // Try to move into data inventory input slots (1–4)
                if (!this.moveItemStackTo(rawStack, 1, 5, false)) {

                    // If from player inventory (5–31), try hotbar (32–40)
                    if (quickMovedSlotIndex < 32) {
                        if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    // If from hotbar (32–40), try player inventory (5–31)
                    else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            // --- Data inventory input slots (1–4) ---
            else {
                if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // --- Finalize movement ---
            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }


    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.stillValidBoolean;
    }
}
