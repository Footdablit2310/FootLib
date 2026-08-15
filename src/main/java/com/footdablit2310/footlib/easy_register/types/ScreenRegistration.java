package com.footdablit2310.footlib.easy_register.types;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;

public record ScreenRegistration(
        MenuType<?> menuType,
        MenuScreens.ScreenConstructor<?, ?> factory
) {}
