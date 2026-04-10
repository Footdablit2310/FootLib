package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateEmptyingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateEmptyingJsonUtil() {
        type("create:emptying");
    }

    public static CreateEmptyingJsonUtil create() {
        return new CreateEmptyingJsonUtil();
    }
}
