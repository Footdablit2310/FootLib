package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateHauntingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateHauntingJsonUtil() {
        type("create:haunting");
    }

    public static CreateHauntingJsonUtil create() {
        return new CreateHauntingJsonUtil();
    }
}
