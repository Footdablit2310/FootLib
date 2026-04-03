package com.footdablit2310.footlib.common;

import net.minecraft.resources.ResourceLocation;

public final class RecipeUtil {

    public static ResourceLocation id(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
