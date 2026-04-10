package com.footdablit2310.footlib.api.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("unused")
public class OrganicChainRecipeJsonUtil extends RecipeJsonBuilderUtil {

    private OrganicChainRecipeJsonUtil(String type) {
        type(type);
    }

    public static OrganicChainRecipeJsonUtil press() {
        return new OrganicChainRecipeJsonUtil("footorganicprocessing:pressing");
    }

    public static OrganicChainRecipeJsonUtil squeezer() {
        return new OrganicChainRecipeJsonUtil("footorganicprocessing:squeezing");
    }

    public static OrganicChainRecipeJsonUtil centrifuge() {
        return new OrganicChainRecipeJsonUtil("footorganicprocessing:centrifuging");
    }

    public static OrganicChainRecipeJsonUtil mixer() {
        return new OrganicChainRecipeJsonUtil("footorganicprocessing:mixing");
    }

    public static OrganicChainRecipeJsonUtil heatedBasin() {
        return new OrganicChainRecipeJsonUtil("footorganicprocessing:heated_basin");
    }
}
