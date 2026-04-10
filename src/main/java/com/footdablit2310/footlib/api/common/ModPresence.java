package com.footdablit2310.footlib.api.common;

import net.neoforged.fml.ModList;

public final class ModPresence {

    private ModPresence() {}

    public static boolean isInstalled(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isFootEconomyInstalled() {
        return isInstalled("foot_economy");
    }

    public static boolean isCreateTrainFaresInstalled() {
        return isInstalled("create_train_fares");
    }
    public static boolean isFootOrganicProcessingInstalled() {
        return isInstalled("footorganicprocessing");
    }
}