package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class FERItemBuilder<T extends Item> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> factory;

    public FERItemBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    public DeferredHolder<Item, T> register() {
        return reg.items.register(name, factory);
    }
}
