package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;

public class CreateFillingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateFillingJsonUtil() {
        type("create:filling");
    }

    public static CreateFillingJsonUtil create() {
        return new CreateFillingJsonUtil();
    }
}
