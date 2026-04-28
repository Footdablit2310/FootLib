package com.footdablit2310.footlib.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class StructurePreviewPacket {

    private final BlockPos anchor;
    private final List<BlockPos> relPositions;
    private final List<BlockState> states;
    private final boolean full;

    public StructurePreviewPacket(BlockPos anchor,
                                  List<BlockPos> relPositions,
                                  List<BlockState> states,
                                  boolean full) {
        this.anchor = anchor;
        this.relPositions = relPositions;
        this.states = states;
        this.full = full;
    }

    // --------------------------
    // ENCODE
    // --------------------------
    @Deprecated(since = "0.0.1.3", forRemoval = false)
    public static void encode(StructurePreviewPacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.anchor);
        buf.writeBoolean(pkt.full);

        int size = pkt.relPositions.size();
        buf.writeVarInt(size);

        for (int i = 0; i < size; i++) {
            buf.writeBlockPos(pkt.relPositions.get(i));
            buf.writeWithCodec(NbtOps.INSTANCE, BlockState.CODEC, pkt.states.get(i));
        }
    }

    // --------------------------
    // DECODE
    // --------------------------
    @Deprecated(since = "0.0.1.3", forRemoval = false)
    public static StructurePreviewPacket decode(FriendlyByteBuf buf) {
        BlockPos anchor = buf.readBlockPos();
        boolean full = buf.readBoolean();

        int size = buf.readVarInt();
        List<BlockPos> rel = new ArrayList<>(size);
        List<BlockState> states = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            rel.add(buf.readBlockPos());
            states.add(buf.readWithCodec(NbtOps.INSTANCE, BlockState.CODEC, NbtAccounter.unlimitedHeap()));
        }

        return new StructurePreviewPacket(anchor, rel, states, full);
    }

    // --------------------------
    // CLIENT APPLY
    // --------------------------
    public void applyClient() {
        com.footdablit2310.footlib.client.StructurePreviewState.set(
                anchor,
                relPositions,
                states,
                full
        );
    }
}
