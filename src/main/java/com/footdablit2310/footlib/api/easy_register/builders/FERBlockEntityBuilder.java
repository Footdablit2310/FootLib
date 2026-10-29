package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class FERBlockEntityBuilder<T extends BlockEntity> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final BlockEntityType.BlockEntitySupplier<T> factory;
    private final List<Supplier<? extends Block>> validBlocks = new ArrayList<>();

    public FERBlockEntityBuilder(
        FootEasyRegisterSystem reg,
        String name,
        BlockEntityType.BlockEntitySupplier<T> factory
    ) {
        this.reg = Objects.requireNonNull(reg);
        this.name = Objects.requireNonNull(name);
        this.factory = Objects.requireNonNull(factory);
    }

    public FERBlockEntityBuilder<T> validBlock(
        Supplier<? extends Block> block
    ) {
        validBlocks.add(Objects.requireNonNull(block));
        return this;
    }

    @SafeVarargs
    public final FERBlockEntityBuilder<T> validBlocks(
        Supplier<? extends Block>... blocks
    ) {
        Arrays.stream(blocks)
            .map(Objects::requireNonNull)
            .forEach(validBlocks::add);

        return this;
    }

    public DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register() {
        if (validBlocks.isEmpty()) {
            throw new IllegalStateException(
                "Block entity '" + name + "' has no valid blocks"
            );
        }

        return reg.blockEntities.register(name, () -> {
            Block[] resolvedBlocks = validBlocks.stream()
                .map(Supplier::get)
                .toArray(Block[]::new);

            return BlockEntityType.Builder.of(
                factory,
                resolvedBlocks
            ).build(null);
        });
    }
}