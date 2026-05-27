package com.footdablit2310.footlib.easy_register;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.footdablit2310.footlib.easy_register.builders.*;
import java.util.function.Supplier;

public final class FootEasyRegisterSystem {

    public final DeferredRegister<Block> blocks;
    public final DeferredRegister<Item> items;
    public final DeferredRegister<BlockEntityType<?>> blockEntities;

    public final DeferredRegister<CreativeModeTab> tabs;
    public final DeferredRegister<FluidType> fluidTypes;
    public final DeferredRegister<Fluid> fluids;
    public final DeferredRegister<MenuType<?>> menus;

    private final String modId;

    public FootEasyRegisterSystem(String modId, IEventBus bus) {
        this.modId = modId;

        blocks      = DeferredRegister.create(Registries.BLOCK, modId);
        items       = DeferredRegister.create(Registries.ITEM, modId);
        blockEntities = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
        tabs        = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        fluidTypes  = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, modId);
        fluids      = DeferredRegister.create(Registries.FLUID, modId);
        menus       = DeferredRegister.create(Registries.MENU, modId);

        blocks.register(bus);
        items.register(bus);
        blockEntities.register(bus);
        tabs.register(bus);
        fluidTypes.register(bus);
        fluids.register(bus);
        menus.register(bus);
    }

    public String getModId() {
        return modId;
    }
    // ------------------------------------------------------------
    // ⭐ BUILDER ENTRY POINTS
    // ------------------------------------------------------------

    public <T extends Block> FERBlockBuilder<T> block(String name, Supplier<T> factory) {
        return new FERBlockBuilder<>(this, name, factory);
    }

    public <T extends Item> FERItemBuilder<T> item(String name, Supplier<T> factory) {
        return new FERItemBuilder<>(this, name, factory);
    }

    public <T extends BlockEntity> FERBlockEntityBuilder<T> blockEntity(
            String name,
            BlockEntityType.BlockEntitySupplier<T> factory
    ) {
        return new FERBlockEntityBuilder<>(this, name, factory);
    }

    public <T extends MenuType<?>> FERMenuBuilder<T> menu(String name, Supplier<T> factory) {
        return new FERMenuBuilder<>(this, name, factory);
    }

    public FERCreativeTabBuilder creativeTab(String name) {
        return new FERCreativeTabBuilder(this, name);
    }
    public <S extends FlowingFluid, F extends FlowingFluid> FERFluidBuilder<S, F> fluid(String name) {
        return new FERFluidBuilder<S, F>(this, name);
    }

}
