package com.footdablit2310.footlib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public interface IMultiblockType {
    ResourceLocation getId();

    /**
     * Validate the structure at this position.
     * The implementation should inspect the world and BE.
     * DO NOT mutate the BE here.
     */
    ValidationResult validate(Level level, BlockPos controllerPos, BlockEntity controllerBE);

    /**
     * Form the multiblock. BE may be mutated here.
     */
    void form(Level level, BlockPos controllerPos, BlockEntity controllerBE);

    void breakMultiblock(Level level, BlockPos controllerPos, BlockEntity controllerBE);

    List<BlockPos> getStructureBlocks(Level level, BlockPos controllerPos, BlockEntity controllerBE);
}