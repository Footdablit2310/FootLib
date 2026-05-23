package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public final class FERMenuBuilder<T extends MenuType<?>> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> factory;

    public FERMenuBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    public DeferredHolder<MenuType<?>, T> register() {
        return reg.menus.register(name, factory);
    }
}
