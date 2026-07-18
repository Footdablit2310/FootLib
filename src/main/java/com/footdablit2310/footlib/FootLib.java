package com.footdablit2310.footlib;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(FootLib.MOD_ID)
public final class FootLib {

    public static final String MOD_ID = "footlib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> FOOTLIB_TAB;

    public FootLib(IEventBus modBus) {}
}

