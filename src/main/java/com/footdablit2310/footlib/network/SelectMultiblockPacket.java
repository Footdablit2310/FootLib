package com.footdablit2310.footlib.network;

import com.footdablit2310.footlib.blockentity.MultiblockController;
import com.footdablit2310.footlib.multiblock.FootLibMultiblockUtil;
import com.footdablit2310.footlib.multiblock.MultiBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SelectMultiblockPacket {

    private final BlockPos controllerPos;
    private final ResourceLocation multiblockId;

    public SelectMultiblockPacket(BlockPos pos, ResourceLocation id) {
        this.controllerPos = pos;
        this.multiblockId = id;
    }

    public SelectMultiblockPacket(FriendlyByteBuf buf) {
        this.controllerPos = buf.readBlockPos();
        this.multiblockId = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(controllerPos);
        buf.writeResourceLocation(multiblockId);
    }

    // ---------------------------
    // SERVER HANDLER
    // ---------------------------
    public void handle(ServerPlayer player) {
        Level level = player.level();
        BlockEntity be = level.getBlockEntity(controllerPos);

        if (!(be instanceof MultiblockController controller)) {
            return;
        }

        MultiBlockData data = FootLibMultiblockUtil.getById(multiblockId);
        if (data == null) {
            return;
        }

        // Fast-path: block count
        if (!FootLibMultiblockUtil.validateCounts(level, controllerPos, data)) {
            return;
        }

        // Slow-path: positional validation
        if (!FootLibMultiblockUtil.validatePositions(level, controllerPos, data)) {
            return;
        }

        // Activate controller
        controller.activate(multiblockId, controllerPos);
    }
}
