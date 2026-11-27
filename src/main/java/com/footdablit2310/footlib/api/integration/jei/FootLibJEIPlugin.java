package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.FootLib;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class FootLibJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FootLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        JEIRecipeRegistry.getRecipes().forEach((id, recipes) -> {
            RecipeType<JEIRecipeWrapper> type =
                    RecipeType.create(id.getNamespace(), id.getPath(), JEIRecipeWrapper.class);
            registration.addRecipes(type, recipes);
        });
    }
}
