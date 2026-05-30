package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public final class FERBlockBuilder<T extends Block> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> blockFactory;

    private boolean makeItem = false;
    private Function<Item.Properties, Item> itemFactory;

    private DeferredHolder<Block, T> blockHolder;

    //Creative tab
    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;

    public FERBlockBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> blockFactory) {
        this.reg = reg;
        this.name = name;
        this.blockFactory = blockFactory;
    }

    public FERBlockBuilder<T> simpleItem() {
        this.makeItem = true;
        this.itemFactory = props -> new BlockItem(blockHolder.get(), props);
        return this;
    }

    public FERBlockBuilder<T> item(Function<Item.Properties, Item> factory) {
        this.makeItem = true;
        this.itemFactory = factory;
        return this;
    }

    public DeferredHolder<Block, T> register() {

        blockHolder = reg.blocks.register(name, blockFactory);

        if (makeItem) {
            reg.items.register(name, () -> itemFactory.apply(new Item.Properties()));
        }
        if (addToCreativeTab) {

            if (explicitTab != null) {
                reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(blockHolder.get()));
            }
            else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Block '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }

                reg.tryAddToCreativeTab(() -> new ItemStack(blockHolder.get()));
            }
        }


        return blockHolder;
    }
}
