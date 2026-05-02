package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.basic.RecipeJsonBuilderUtil;

public class CreateDeployingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateDeployingJsonUtil() {
        type("create:deploying");
    }

    public static CreateDeployingJsonUtil create() {
        return new CreateDeployingJsonUtil();
    }
}
