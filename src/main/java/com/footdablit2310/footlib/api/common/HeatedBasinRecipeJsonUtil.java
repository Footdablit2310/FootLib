package com.footdablit2310.footlib.api.common;

public class HeatedBasinRecipeJsonUtil extends RecipeJsonBuilderUtil {

    private HeatedBasinRecipeJsonUtil() {
        type("footorganicprocessing:heated_basin");
    }

    public static HeatedBasinRecipeJsonUtil create() {
        return new HeatedBasinRecipeJsonUtil();
    }
}
