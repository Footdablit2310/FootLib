package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FERItemBuilder<T extends Item> {

    private final FootEasyRegisterSystem reg;
    private final String name;
    private final Supplier<T> factory;

    private boolean addToCreativeTab = false;
    private CreativeModeTab explicitTab = null;
    private boolean enableDatagen = false;

    private Consumer<RecipeOutput> recipeBuilder = null;

    public FERItemBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    // -------------------------
    // CREATIVE TAB
    // -------------------------
    public FERItemBuilder<T> creativeTab() {
        this.addToCreativeTab = true;
        return this;
    }

    public FERItemBuilder<T> creativeTab(CreativeModeTab tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    // -------------------------
    // DATAGEN
    // -------------------------
    public FERItemBuilder<T> datagen() {
        this.enableDatagen = true;
        return this;
    }

    // -------------------------
    // RECIPE
    // -------------------------
    public FERItemBuilder<T> recipe(Consumer<RecipeOutput> builder) {
        this.recipeBuilder = builder;
        return this;
    }

    // -------------------------
    // REGISTER
    // -------------------------
    public DeferredHolder<Item, T> register() {

        DeferredHolder<Item, T> holder = reg.items.register(name, factory);

        // CREATIVE TAB
        if (addToCreativeTab) {
            if (explicitTab != null) {
                reg.addToSpecificCreativeTab(explicitTab, () -> new ItemStack(holder.get()));
            } else {
                if (!reg.hasCreativeTab()) {
                    throw new IllegalStateException(
                        "Item '" + name + "' called .creativeTab() but no FER creative tab exists."
                    );
                }
                reg.tryAddToCreativeTab(() -> new ItemStack(holder.get()));
            }
        }

        // DATAGEN
        if (enableDatagen) {
            reg.registerItemModel(name, holder.get());
            reg.registerLang(holder.get().getDescriptionId(),
                FootEasyRegisterSystem.SnakeToPascalCase(name));
        }

        // RECIPE
        if (recipeBuilder != null) {
            reg.registerRecipe(recipeBuilder);
        }

        return holder;
    }
}
