package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;

public class CreateCrushingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateCrushingJsonUtil() {
        type("create:crushing");
    }

    public static CreateCrushingJsonUtil create() {
        return new CreateCrushingJsonUtil();
    }
}
