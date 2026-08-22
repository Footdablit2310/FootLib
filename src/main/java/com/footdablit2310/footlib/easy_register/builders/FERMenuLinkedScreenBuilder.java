package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.easy_register.types.ScreenRegistration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class FERMenuLinkedScreenBuilder<ETACM extends AbstractContainerMenu> {

    private final FootEasyRegisterSystem reg;
    private final MenuType<ETACM> menuType;

    private MenuScreens.ScreenConstructor<?, ?> factory;

    public FERMenuLinkedScreenBuilder(FootEasyRegisterSystem reg, MenuType<ETACM> menuType) {
        this.reg = reg;
        this.menuType = menuType;
    }

    public FERMenuLinkedScreenBuilder<ETACM> factory(MenuScreens.ScreenConstructor<ETACM, ? extends MenuAccess<ETACM>>factory) {
        this.factory = factory;
        return this;
    }

    public ScreenRegistration<ETACM> build() {
        if (factory == null)
            throw new IllegalStateException("Screen builder missing factory()");

        ScreenRegistration<ETACM> registration =
                new ScreenRegistration<>(menuType, factory);

        reg.addScreen(registration);
        return registration;
    }
}
