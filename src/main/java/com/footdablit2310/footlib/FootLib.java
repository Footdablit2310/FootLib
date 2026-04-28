package com.footdablit2310.footlib;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

import com.footdablit2310.footlib.api.common.commands.FootLibModListCommand;
import com.footdablit2310.footlib.api.common.commands.FootLibVisualizeStructureCommand;
import com.footdablit2310.footlib.api.integration.create.CreateCompat;
import com.footdablit2310.footlib.api.integration.jei.JEICompat;

@Mod(FootLib.MOD_ID)
public final class FootLib {

    public static final String MOD_ID = "footlib";

    public FootLib(IEventBus modBus) {

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
}

