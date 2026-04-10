package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreateSandpaperPolishingJsonUtil extends RecipeJsonBuilderUtil {

    private CreateSandpaperPolishingJsonUtil() {
        type("create:sandpaper_polishing");
    }

    public static CreateSandpaperPolishingJsonUtil create() {
        return new CreateSandpaperPolishingJsonUtil();
    }
}
