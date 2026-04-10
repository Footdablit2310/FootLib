package com.footdablit2310.footlib.api.integration.jei;

import mezz.jei.api.recipe.RecipeType;

public final class JEICategoryBuilder {

    private JEICategoryBuilder() {}

    public static <T> RecipeType<T> type(String uid, Class<T> clazz) {
        String[] parts = uid.split(":", 2);
        String namespace = parts[0];
        String path = parts.length > 1 ? parts[1] : "";
        return RecipeType.create(namespace, path, clazz);
    }
}
