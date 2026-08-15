package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.easy_register.types.ScreenRegistration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class FERScreenBuilder<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> {

    private final FootEasyRegisterSystem reg;
    private final MenuType<M> menuType;

    private MenuScreens.ScreenConstructor<M, S> factory;

    public FERScreenBuilder(FootEasyRegisterSystem reg, MenuType<M> menuType) {
        this.reg = reg;
        this.menuType = menuType;
    }

    public FERScreenBuilder<M, S> factory(MenuScreens.ScreenConstructor<M, S> factory) {
        this.factory = factory;
        return this;
    }

    public ScreenRegistration build() {
        if (factory == null)
            throw new IllegalStateException("Screen builder missing factory()");

        ScreenRegistration registration =
                new ScreenRegistration(menuType, factory);

        reg.addScreen(registration);
        return registration;
    }
}
