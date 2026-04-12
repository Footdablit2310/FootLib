package com.footdablit2310.footlib.api.compat_connect.create_footop;

import com.footdablit2310.footlib.api.integration.create.CreateMixingJsonUtil;
import com.footdablit2310.footlib.api.integration.create.CreateHeatUtil;
import com.footdablit2310.footlib.api.common.ModPresence;
import com.google.gson.JsonObject;

public final class PTFHeatedCreateCompat {

    private PTFHeatedCreateCompat() {}

    public static void convert(JsonObject ptfRecipe) {
        if (!ModPresence.isInstalled("footorganicprocessing")) return;

        String heat = ptfRecipe.get("heat").getAsString();

        if (!CreateHeatUtil.isCreateCompatible(heat)) {
            // ultraheated → FootOP only
            return;
        }

        String createHeat = CreateHeatUtil.mapToCreate(heat);

        CreateMixingJsonUtil mix = CreateMixingJsonUtil.create();

        if (createHeat.equals("heated")) mix.heated();
        if (createHeat.equals("superheated")) mix.superHeated();

        mix.inputs(ptfRecipe.getAsJsonArray("ingredients"))
           .outputs(ptfRecipe.getAsJsonArray("results"))
           .build();
    }
}
