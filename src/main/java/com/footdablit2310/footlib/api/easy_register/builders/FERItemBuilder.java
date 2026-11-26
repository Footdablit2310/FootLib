package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.api.integration.jei.JEIRecipeData;
import com.footdablit2310.footlib.api.integration.jei.JEIRecipeRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FERItemBuilder<T extends Item> {

    private final @NotNull FootEasyRegisterSystem reg;
    private final @NotNull String name;
    private final @NotNull Supplier<T> factory;

    private boolean addToCreativeTab = false;
    private @Nullable ResourceKey<CreativeModeTab> explicitTab = null;
    private @Nullable String tabName = null;
    private final List<JEIRecipeData> jeiRecipes = new ArrayList<>();

    public FERItemBuilder(@NotNull FootEasyRegisterSystem reg, @NotNull String name, @NotNull Supplier<T> factory) {
        this.reg = reg;
        this.name = name;
        this.factory = factory;
    }

    // -------------------------
    // CREATIVE TAB
    // -------------------------
    public @NotNull FERItemBuilder<T> creativeTab(@NotNull String tabName) {
        this.addToCreativeTab = true;
        this.tabName = tabName;
        return this;
    }

    public @NotNull FERItemBuilder<T> creativeTab(@NotNull ResourceKey<CreativeModeTab> tab) {
        this.addToCreativeTab = true;
        this.explicitTab = tab;
        return this;
    }

    /**
     * Adds a JEI recipe associated with this item. The recipe type must match
     * the type used by the mod's own JEI recipe category.
     */
    public @NotNull FERItemBuilder<T> jeiRecipe(
            @NotNull ResourceLocation recipeType,
            @NotNull List<ItemStack> inputs,
            @NotNull List<ItemStack> outputs
    ) {
        jeiRecipes.add(new JEIRecipeData(recipeType, inputs, outputs));
        return this;
    }

    public @NotNull DeferredHolder<Item, T> register() {
        DeferredHolder<Item, T> holder = reg.items.register(name, factory);

        if (addToCreativeTab) {
            if (explicitTab != null) {
                reg.addToExistingTab(explicitTab, () -> new ItemStack(holder));
            } else {
                reg.addToTab(tabName, () -> new ItemStack(holder));
            }
        }

        jeiRecipes.forEach(JEIRecipeRegistry::register);
        return holder;
    }
}
