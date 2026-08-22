package com.footdablit2310.footlib.registry.custom.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.Map;


public record MultiblockData<BE extends BlockEntity> (
        BlockEntityType<BE> controllerBlockEntityType,
        Map<Block, List<BlockPos>> blockPositions
) {
    public static final Codec<MultiblockData<?>> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()
                            .fieldOf("controller")
                            .forGetter(MultiblockData::controllerBlockEntityType),

                    Codec.unboundedMap(
                                    BuiltInRegistries.BLOCK.byNameCodec(),
                                    BlockPos.CODEC.listOf()
                            ).fieldOf("positions")
                            .forGetter(MultiblockData::blockPositions)
            ).apply(instance, MultiblockData::new));




}