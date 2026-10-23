package com.footdablit2310.footlib.api.common.codec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BlockBasedCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockPos> BLOCK_POS_CODEC =
            StreamCodec.of(
                    (buf, pos) -> buf.writeBlockPos(pos),
                    buf -> buf.readBlockPos()
            );

}
