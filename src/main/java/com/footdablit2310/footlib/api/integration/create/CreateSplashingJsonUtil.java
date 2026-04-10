package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateSplashingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateSplashingJsonUtil() {
        type("create:splashing");
    }

    public static CreateSplashingJsonUtil create() {
        return new CreateSplashingJsonUtil();
    }
}
