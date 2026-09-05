package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import net.minecraft.resources.ResourceKey;
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
    private ResourceKey<CreativeModeTab> explicitTab = null;
    private boolean enableDatagen = false;

    private String tabName;
    private Consumer<RecipeOutput> recipeBuilder = null;

    public FERItemBuilder(FootEasyRegisterSystem reg, String name, Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    // -------------------------
    // CREATIVE TAB
    // -------------------------
    public FERItemBuilder<T> creativeTab(String tabName) {
        this.addToCreativeTab = true;
        this.tabName = tabName;
        return this;
    }

    public FERItemBuilder<T> creativeTab(ResourceKey<CreativeModeTab> tab) {
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
                reg.addToExistingTab(explicitTab, () -> new ItemStack(holder));
            } else {
                reg.addToTab(tabName, () -> new ItemStack(holder));
            }
        }

        // DATAGEN
        if (enableDatagen) {
            reg.registerItemModel(name, holder.get());
            reg.registerLang(holder.get().getDescriptionId(),
                FootEasyRegisterSystem.SnakeToNormalCase(name));
        }

        // RECIPE
        if (recipeBuilder != null) {
            reg.registerRecipe(recipeBuilder);
        }

        return holder;
    }
}
