package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FERBlockBuilder<T extends Block> {

    private final @NotNull FootEasyRegisterSystem reg;
    private final @NotNull String name;
    private final @NotNull Supplier<T> factory;

    private boolean addToCreativeTab = false;
    private @Nullable ResourceKey<CreativeModeTab> explicitTab = null;
    private boolean createSimpleItem = false;
    private @Nullable String tabName = null;


	public FERBlockBuilder(@NotNull FootEasyRegisterSystem reg, @NotNull String name, @NotNull Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    //CREATIVE TAB
    public @NotNull FERBlockBuilder<T> creativeTab(@NotNull String tabName) {
        this.addToCreativeTab = true;
        this.tabName = tabName;
        return this;
    }

    public @NotNull FERBlockBuilder<T> creativeTab(@NotNull ResourceKey<CreativeModeTab> tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    //SIMPLE ITEM
    public FERBlockBuilder<T> simpleItem() {
        // Register a BlockItem for this block
        this.createSimpleItem = true;
        return this;
    }

    //REGISTER
    public DeferredHolder<Block, T> register() {

        DeferredHolder<Block, T> holder = reg.blocks.register(name, factory);

        // --- SIMPLE ITEM ---
        if (createSimpleItem) {
            reg.items.register(name, () ->
                new BlockItem(holder.get(), new net.minecraft.world.item.Item.Properties())
            );
        }

        // --- CREATIVE TAB ---
        if (addToCreativeTab) {
            Supplier<ItemStack> stackSupplier = () ->
                new ItemStack(createSimpleItem ? holder.get().asItem() : holder.get());

            if (explicitTab != null) {
                reg.addToExistingTab(explicitTab, stackSupplier);
            }
            reg.addToTab(tabName, stackSupplier);
        }

        return holder;
    }


}
