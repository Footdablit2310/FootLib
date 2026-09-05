package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class FERBlockEntityBuilder<T extends BlockEntity> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final BlockEntityType.BlockEntitySupplier<T> factory;
    private final List<Supplier<? extends Block>> validBlocks = new ArrayList<>();

    public FERBlockEntityBuilder(FootEasyRegisterSystem reg, String name, BlockEntityType.BlockEntitySupplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    public FERBlockEntityBuilder<T> validBlock(Supplier<? extends Block> block) {
        validBlocks.add(block);
        return this;
    }

    public DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register() {
        return reg.blockEntities.register(name, () ->
            BlockEntityType.Builder.of(factory,
                validBlocks.stream().map(Supplier::get).toArray(Block[]::new)
            ).build(null) //Null is here because we do not need mojang DataFixers here.
        );
    }
}
