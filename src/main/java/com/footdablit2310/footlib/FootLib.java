package com.footdablit2310.footlib;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import com.footdablit2310.footlib.api.integration.create.CreateCompat;
import com.footdablit2310.footlib.api.integration.jei.JEICompat;

@Mod(FootLib.MOD_ID)
public final class FootLib {

    public static final String MOD_ID = "footlib";

    public FootLib(IEventBus bus) {
        // Initialization logic goes here
        CreateCompat.init(); // Initializes Create if create is installed
        JEICompat.init(); // Initializes JEI if JEI is installed
    }
}