package com.footdablit2310.footlib.api.common.basic;

public class PTFRecipeJsonUtil extends RecipeJsonBuilderUtil {

    private PTFRecipeJsonUtil(String type) {
        type(type);
    }

    public static PTFRecipeJsonUtil heated() {
        return new PTFRecipeJsonUtil("footorganicprocessing:ptf_heated");
    }

    public static PTFRecipeJsonUtil superheated() {
        return new PTFRecipeJsonUtil("footorganicprocessing:ptf_superheated");
    }
}
