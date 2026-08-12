package com.footdablit2310.footlib;

import com.footdablit2310.footlib.multiblock.IMultiblockType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class FootLibRegistries {
    public static final ResourceKey<Registry<IMultiblockType>> MULTIBLOCK_TYPE_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("footlib", "multiblock_types"));

    public static Registry<IMultiblockType> MULTIBLOCK_TYPES;

    public static void registerRegistries(NewRegistryEvent event) {
        MULTIBLOCK_TYPES = event.create(
                new RegistryBuilder<>(MULTIBLOCK_TYPE_KEY)
                        .sync(true)
                        .defaultKey(ResourceLocation.fromNamespaceAndPath("footlib", "empty"))
        );
    }
}