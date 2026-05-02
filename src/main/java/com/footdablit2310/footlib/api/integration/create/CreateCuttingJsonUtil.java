package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;

public class CreateCuttingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateCuttingJsonUtil() {
        type("create:cutting");
    }

    public static CreateCuttingJsonUtil create() {
        return new CreateCuttingJsonUtil();
    }
}
