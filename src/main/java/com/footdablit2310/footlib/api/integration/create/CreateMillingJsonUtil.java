package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateMillingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateMillingJsonUtil() {
        type("create:milling");
    }

    public static CreateMillingJsonUtil create() {
        return new CreateMillingJsonUtil();
    }
}
