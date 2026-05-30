package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class FERItemBuilder<T extends Item> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> factory;

    //Creative tab
    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;


    public FERItemBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }
    public FERItemBuilder<T> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }
    public FERItemBuilder<T> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    public DeferredHolder<Item, T> register() {
        DeferredHolder<Item, T> holder = reg.items.register(name, factory);
        if (addToCreativeTab) {
            if (explicitTab != null) {
                // explicit tab always wins
                reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(holder.get()));
            }
            else {
                // no explicit tab → must use FER tab
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Item '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }

                reg.tryAddToCreativeTab(() -> new ItemStack(holder.get()));
            }
        }

        return holder;
    }
}
