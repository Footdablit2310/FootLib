package com.footdablit2310.footlib.easy_register.types;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record ScreenRegistration<ETACM extends AbstractContainerMenu>(
        MenuType<ETACM> menuType,
        MenuScreens.ScreenConstructor<?, ?> factory
) {}
