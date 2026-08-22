package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;

import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class FERMenuBuilder<T extends AbstractContainerMenu> {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private Object constructor; // MenuSupplier<T> OR IContainerFactory<T>

    private DeferredHolder<MenuType<?>, MenuType<T>> holder;

    private FeatureFlagSet featureFlags = FeatureFlags.DEFAULT_FLAGS;

    public FERMenuBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    /** Accepts MenuSupplier<T> */
    public FERMenuBuilder<T> supplier(MenuType.MenuSupplier<T> supplier) {
        this.constructor = supplier;
        return this;
    }

    /** Accepts IContainerFactory<T> */
    public FERMenuBuilder<T> factory(IContainerFactory<T> factory) {
        this.constructor = factory;
        return this;
    }

    /**
     * This allows you to set your FeatureFlags before construction.
     */
    public FERMenuBuilder<T> setFeatureFlags(FeatureFlagSet featureFlags){
        this.featureFlags = featureFlags;
        return this;
    }

    /** Register MenuType<T> */
    @SuppressWarnings("unchecked")
    public DeferredHolder<MenuType<?>, MenuType<T>> register() {
        if (constructor == null) {
            throw new IllegalStateException("Menu '" + name + "' is missing a supplier() or factory()");
        }

        holder = reg.menus.register(name, () ->
                new MenuType<>(
                        (MenuType.MenuSupplier<T>) constructor,
                        this.featureFlags
                )
        );

        return holder;
    }


}