package com.footdablit2310.footlib;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;

import org.slf4j.Logger;

import com.footdablit2310.footlib.api.common.commands.FootLibModListCommand;
import com.footdablit2310.footlib.api.common.commands.FootLibVisualizeStructureCommand;
import com.footdablit2310.footlib.api.integration.create.CreateCompat;
import com.footdablit2310.footlib.api.integration.jei.JEICompat;
import com.mojang.logging.LogUtils;

@Mod(FootLib.MOD_ID)
public final class FootLib {

    public static final String MOD_ID = "footlib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> FOOTLIB_TAB;
    public static FootEasyRegisterSystem FER;

    public FootLib(IEventBus modBus) {
        FER = new FootEasyRegisterSystem("footlib", modBus, "FootLib");
        FER.setActiveCreativeTab(FER.creativeTab("").searchBar().title(Component.translatable("FootLib"))).register();
        // Register commands
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
            FootLibModListCommand::register
        );
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
            FootLibVisualizeStructureCommand::register
        );

        // Optional integrations
        CreateCompat.init();
        JEICompat.init();
        
    }
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
    }

}

