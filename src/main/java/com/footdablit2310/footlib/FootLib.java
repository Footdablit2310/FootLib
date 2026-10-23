package com.footdablit2310.footlib;

import com.footdablit2310.footlib.api.common.json_config.events.FootLibConfigRegisterEvent;
import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.api.easy_register.config.TabConfig;
import com.footdablit2310.footlib.json_configs.ModJsonConfigs;
import com.footdablit2310.footlib.registry.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import com.footdablit2310.footlib.api.common.commands.FootLibModListCommand;
import com.footdablit2310.footlib.api.common.commands.FootLibVisualizeStructureCommand;
import com.footdablit2310.footlib.api.integration.create.CreateCompat;
import com.footdablit2310.footlib.api.integration.jei.JEICompat;
import com.mojang.logging.LogUtils;

import java.util.List;

@Mod(FootLib.MOD_ID)
public final class FootLib {

    public static final String MOD_ID = "footlib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static FootEasyRegisterSystem FER;

    public FootLib(IEventBus modBus) {
        FER = new FootEasyRegisterSystem("footlib", modBus, "FootLib");
        // Register commands
        NeoForge.EVENT_BUS.addListener(
            FootLibModListCommand::register
        );
        NeoForge.EVENT_BUS.addListener(
            FootLibVisualizeStructureCommand::register
        );

        // Optional integrations
        CreateCompat.init();
        JEICompat.init();
        NeoForge.EVENT_BUS.post(new FootLibConfigRegisterEvent());

    }
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
    }
    @SubscribeEvent
    public static void registerJsonConfigs(FootLibConfigRegisterEvent event) {
        event.register(ModJsonConfigs.MAINTENANCE);
    }

}

