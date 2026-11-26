package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.FootLib;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JEI entry point that exposes recipe data declared through FER builders.
 * Recipe categories remain the responsibility of the mod adding them.
 */
@JeiPlugin
public final class FootLibJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FootLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Map<ResourceLocation, List<JEIRecipeWrapper>> groupedRecipes = new LinkedHashMap<>();

        for (JEIRecipeData recipe : JEIRecipeRegistry.getRecipes()) {
            groupedRecipes.computeIfAbsent(recipe.recipeType(), ignored -> new ArrayList<>())
                    .add(new JEIRecipeWrapper(recipe.inputs(), recipe.outputs()));
        }

        groupedRecipes.forEach((id, recipes) -> {
            RecipeType<JEIRecipeWrapper> type =
                    RecipeType.create(id.getNamespace(), id.getPath(), JEIRecipeWrapper.class);
            registration.addRecipes(type, List.copyOf(recipes));
        });
    }
}
