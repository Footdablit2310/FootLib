package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.multiblock.IMultiblockType;
import java.util.function.Supplier;

public final class FERMultiBlockBuilder<T extends IMultiblockType> {
    private final FootEasyRegisterSystem system;
    private final String name;
    private Supplier<T> factory;

    public FERMultiBlockBuilder(FootEasyRegisterSystem system, String name) {
        this.system = system;
        this.name = name;
    }

    public FERMultiBlockBuilder<T> type(Supplier<T> factory) {
        this.factory = factory;
        return this;
    }

    public Supplier<T> build() {
        if (factory == null) {
            throw new IllegalStateException("Multiblock type factory not set for: " + name);
        }
        return system.multiblockTypes.register(name, factory);
    }
}