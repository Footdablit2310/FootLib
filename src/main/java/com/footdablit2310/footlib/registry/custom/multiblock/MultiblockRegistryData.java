package com.footdablit2310.footlib.registry.custom.multiblock;

import com.footdablit2310.footlib.FootLib;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public record MultiblockRegistryData<BE extends BlockEntity>(
        MultiblockData<BE> multiblockData,
        Direction direction // STRICT: metadata only, not applied to shape
) {
    private static final Logger logger = FootLib.LOGGER;

    private static BlockPos addRelative(BlockPos controller, BlockPos relative) {
        return controller.offset(relative.getX(), relative.getY(), relative.getZ());
    }

    /**
     * STRICT IMPLEMENTATION:
     * - Shape is NOT rotated
     * - Direction is NOT applied to relative positions
     * - Direction is ONLY used as a filter when selecting multiblocks
     * - Validation checks the shape EXACTLY as defined
     */
    public boolean validate(Level level, BlockEntity blockEntity) {

        BlockEntity be = level.getBlockEntity(blockEntity.getBlockPos());
        if (be == null) {
            logger.warn("Multiblock validation failed: controller BlockEntity is null at {}",
                    blockEntity.getBlockPos().toShortString());
            return false;
        }

        if (be.getType() != multiblockData.controllerBlockEntityType()) {
            logger.warn("Multiblock validation failed: expected controller BE type {}, found {}",
                    multiblockData.controllerBlockEntityType(),
                    be.getType());
            return false;
        }

        BlockPos controllerPos = blockEntity.getBlockPos();

        for (Map.Entry<Block, List<BlockPos>> entry : multiblockData.blockPositions().entrySet()) {
            Block expectedBlock = entry.getKey();

            List<BlockPos> positions = entry.getValue().stream()
                    .map(relativePos -> addRelative(controllerPos, relativePos))
                    .toList();

            for (BlockPos pos : positions) {
                Block actualBlock = level.getBlockState(pos).getBlock();

                if (actualBlock != expectedBlock) {
                    logger.warn(
                            "Multiblock validation failed at {}: expected {}, found {}",
                            pos.toShortString(),
                            expectedBlock.getName().getString(),
                            actualBlock.getName().getString()
                    );
                    return false;
                }
            }
        }

        return true;
    }
    public static final Codec<MultiblockRegistryData<?>> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    MultiblockData.CODEC.fieldOf("data")
                            .forGetter(MultiblockRegistryData::multiblockData),

                    Direction.CODEC.fieldOf("direction")
                            .forGetter(MultiblockRegistryData::direction)
            ).apply(instance, MultiblockRegistryData::new));

}
