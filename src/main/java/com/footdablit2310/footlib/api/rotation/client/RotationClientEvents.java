package com.footdablit2310.footlib.api.rotation.client;

import com.footdablit2310.footlib.FootLib;
import com.footdablit2310.footlib.api.rotation.item.RotationItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class RotationClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(RotationItems.ROTATION_MENU.get(), RotationEditorScreen::new);
    }
}
