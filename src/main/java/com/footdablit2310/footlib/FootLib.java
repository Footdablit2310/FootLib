package com.footdablit2310.footlib;

import com.footdablit2310.footlib.api.common.json_config.events.FootLibConfigRegisterEvent;
import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.api.rotation.RotationConfigManager;
import com.footdablit2310.footlib.api.rotation.client.RotationClientEvents;
import com.footdablit2310.footlib.api.rotation.item.RotationItems;
import com.footdablit2310.footlib.api.rotation.network.RotationSubmitPayload;
import com.footdablit2310.footlib.json_configs.ModJsonConfigs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.footdablit2310.footlib.api.common.commands.FootLibModListCommand;
import com.footdablit2310.footlib.api.common.commands.FootLibVisualizeStructureCommand;
import com.footdablit2310.footlib.api.integration.create.CreateCompat;
import com.mojang.logging.LogUtils;

@Mod(value=FootLib.MOD_ID, dist={Dist.CLIENT, Dist.DEDICATED_SERVER})
public final class FootLib {

    public static final String MOD_ID = "footlib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static FootEasyRegisterSystem FER;

    public FootLib(IEventBus modBus, Dist dist) {
        FER = new FootEasyRegisterSystem(MOD_ID, modBus, "FootLib");
        RotationItems.register();
        modBus.addListener(FootLib::onRegisterPayloads);
        NeoForge.EVENT_BUS.addListener(RotationConfigManager::onAddReloadListeners);
        // Register commands
        NeoForge.EVENT_BUS.addListener(
            FootLibModListCommand::register
        );
        NeoForge.EVENT_BUS.addListener(
            FootLibVisualizeStructureCommand::register
        );
        modBus.addListener(FootLib::registerJsonConfigs);

        if (dist.isClient()) {
            modBus.addListener(RotationClientEvents::registerScreens);
        }

        // Optional integrations
        CreateCompat.init();
        NeoForge.EVENT_BUS.post(new FootLibConfigRegisterEvent());

    }
    @SubscribeEvent
    public static void onRegisterPayloads(@NotNull RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
                RotationSubmitPayload.TYPE,
                RotationSubmitPayload.STREAM_CODEC,
                RotationSubmitPayload::handle
        );
    }
    @SubscribeEvent
    public static void registerJsonConfigs(@NotNull FootLibConfigRegisterEvent event) {
        event.register(ModJsonConfigs.MAINTENANCE);
    }

}

