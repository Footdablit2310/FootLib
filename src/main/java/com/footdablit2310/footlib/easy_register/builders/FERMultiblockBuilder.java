package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.registry.custom.multiblock.MultiblockData;
import com.footdablit2310.footlib.registry.custom.multiblock.MultiblockRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FERMultiblockBuilder<BE extends BlockEntity> {

    private final FootEasyRegisterSystem fer;
    private final BlockEntityType<BE> controllerType;
    private final Map<Block, List<BlockPos>> blockMap = new HashMap<>();
    private Direction direction;

    public FERMultiblockBuilder(FootEasyRegisterSystem fer, BlockEntityType<BE> controllerType) {
        this.fer = fer;
        this.controllerType = controllerType;
    }

    public FERMultiblockBuilder<BE> block(Block block, BlockPos relativePos) {
        blockMap.computeIfAbsent(block, k -> new ArrayList<>()).add(relativePos);
        return this;
    }

    public FERMultiblockBuilder<BE> block(Block block, List<BlockPos> relativePositions) {
        blockMap.computeIfAbsent(block, k -> new ArrayList<>()).addAll(relativePositions);
        return this;
    }
    public FERMultiblockBuilder<BE> direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public DeferredHolder<MultiblockRegistryData<?>, MultiblockRegistryData<BE>> register(String name) {

        // Build immutable map
        Map<Block, List<BlockPos>> immutableMap = new HashMap<>();
        for (Map.Entry<Block, List<BlockPos>> entry : blockMap.entrySet()) {
            immutableMap.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        MultiblockData<BE> data =
                new MultiblockData<>(controllerType, Map.copyOf(immutableMap));

        // Register directly into FootEasyRegisterSystem
        return fer.multiblocks.register(name, () -> new MultiblockRegistryData<>(data, direction));
    }
}
