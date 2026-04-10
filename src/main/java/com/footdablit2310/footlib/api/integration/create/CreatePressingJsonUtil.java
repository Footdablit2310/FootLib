package com.footdablit2310.footlib.api.integration.create;

import com.footdablit2310.footlib.api.common.RecipeJsonBuilderUtil;

public class CreatePressingJsonUtil extends RecipeJsonBuilderUtil {

    private CreatePressingJsonUtil() {
        type("create:pressing");
    }

    public static CreatePressingJsonUtil create() {
        return new CreatePressingJsonUtil();
    }
}
