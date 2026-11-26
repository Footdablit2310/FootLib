package com.footdablit2310.footlib.api.integration.jei;

import com.footdablit2310.footlib.FootLib;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

/**
 * JEI entry point for registrations queued through {@link JEIRegistration}.
 */
@JeiPlugin
public final class FootLibJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FootLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        JEIRegistration.registerCategories(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        JEIRegistration.registerRecipes(registration);
    }
}
