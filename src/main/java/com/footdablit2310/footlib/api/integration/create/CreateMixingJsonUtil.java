package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateMixingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateMixingJsonUtil() {
        type("create:mixing");
    }

    public static CreateMixingJsonUtil create() {
        return new CreateMixingJsonUtil();
    }

    public CreateMixingJsonUtil heatNone() {
        root.addProperty("heatRequirement", "none");
        return this;
    }

    public CreateMixingJsonUtil heated() {
        root.addProperty("heatRequirement", "heated");
        return this;
    }

    public CreateMixingJsonUtil superHeated() {
        root.addProperty("heatRequirement", "superheated");
        return this;
    }
}
