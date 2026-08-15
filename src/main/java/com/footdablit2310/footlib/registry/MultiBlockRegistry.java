package com.footdablit2310.footlib.registry;

import com.footdablit2310.footlib.multiblock.MultiBlockData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class MultiBlockRegistry {

    public static final ResourceKey<Registry<MultiBlockData>> MULTIBLOCK_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath("footlib", "multiblock")
            );

    public static final Registry<MultiBlockData> MULTIBLOCK_REGISTRY =
            new RegistryBuilder<>(MULTIBLOCK_REGISTRY_KEY)
                    .create();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(MULTIBLOCK_REGISTRY);
    }

    private MultiBlockRegistry() {}
}
