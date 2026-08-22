package com.footdablit2310.footlib.registry.custom.multiblock;

import com.footdablit2310.footlib.FootLib;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public final class CustomMultiblockRegistry {

    public static final DeferredRegister<MultiblockRegistryData<?>> MULTIBLOCKS =
            DeferredRegister.create(CustomMultiblockRegistryKeys.MULTIBLOCK_REGISTRY_KEY, FootLib.MOD_ID);

    public static void init(IEventBus modBus) {
        MULTIBLOCKS.register(modBus);
    }

}
