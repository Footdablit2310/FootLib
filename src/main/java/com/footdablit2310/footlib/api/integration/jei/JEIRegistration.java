package com.footdablit2310.footlib.api.integration.jei;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Central registration point for JEI categories and their recipes.
 *
 * <p>Call these methods from client-side mod initialization. The FootLib JEI
 * plugin invokes the queued registrations during JEI's category and recipe
 * registration phases.</p>
 */
public final class JEIRegistration {

    private static final List<Consumer<IRecipeCategoryRegistration>> CATEGORIES =
            new CopyOnWriteArrayList<>();
    private static final List<Consumer<IRecipeRegistration>> RECIPES =
            new CopyOnWriteArrayList<>();

    private JEIRegistration() {}

    /**
     * Queue a category factory. The factory receives JEI's GUI helper so the
     * category can create its background, icon, and other drawables.
     */
    public static void registerCategory(
            Function<IGuiHelper, ? extends IRecipeCategory<?>> categoryFactory
    ) {
        Objects.requireNonNull(categoryFactory, "categoryFactory");

        CATEGORIES.add(registration -> registration.addRecipeCategories(
                Objects.requireNonNull(
                        categoryFactory.apply(registration.getJeiHelpers().getGuiHelper()),
                        "categoryFactory returned null"
                )
        ));
    }

    /**
     * Queue recipes for a category. The supplier is evaluated when JEI asks
     * FootLib to register recipes.
     */
    public static <T> void registerRecipes(
            RecipeType<T> recipeType,
            Supplier<? extends Collection<T>> recipes
    ) {
        Objects.requireNonNull(recipeType, "recipeType");
        Objects.requireNonNull(recipes, "recipes");

        RECIPES.add(registration -> addRecipes(registration, recipeType, recipes.get()));
    }

    private static <T> void addRecipes(
            IRecipeRegistration registration,
            RecipeType<T> recipeType,
            Collection<T> recipes
    ) {
        registration.addRecipes(recipeType, List.copyOf(
                Objects.requireNonNull(recipes, "recipes supplier returned null")
        ));
    }

    static void registerCategories(IRecipeCategoryRegistration registration) {
        CATEGORIES.forEach(category -> category.accept(registration));
    }

    static void registerRecipes(IRecipeRegistration registration) {
        RECIPES.forEach(recipes -> recipes.accept(registration));
    }
}
