package com.footdablit2310.footlib.api.common;

import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;

import java.util.ArrayList;
import java.util.List;

public final class ModPresence {

    private ModPresence() {}

    /**
     * Check if a mod is installed by mod ID.
     */
    public static boolean isInstalled(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Returns a List<String> of all installed mod IDs.
     */
    public static List<String> ReturnAllInstalledModIdsAsString() {
        List<String> ids = new ArrayList<>();
        for (ModContainer mod : ModList.get().getSortedMods()) {
            ids.add(mod.getModId());
        }
        return ids;
    }

    /**
     * Returns a List<ModContainer> of all installed mods.
     */
    public static List<ModContainer> ReturnAllInstalledModContainerAsList() {
        return ModList.get().getSortedMods();
    }

    // Other mod IDs
    public static final String CREATE = "create";
    public static final String JEI = "jei";
    public static final String EMI = "emi";
    public static final String CREATE_RAILWAYS_NAVIGATOR = "createrailwaysnavigator";
    public static final String PONDER = "ponder";
    public static final String FLYWHEEL = "flywheel";

    // Your mods
    public static final String FOOTLIB = "footlib";
    public static final String FOOT_DEVICES = "footdevices";
    public static final String FOOT_ELECTRICITY = "footelectricity";
    public static final String FOOT_ECONOMY = "footeconomy";
    public static final String FOOT_ORGNIC_PROCESSING = "footorganicprocessing";
    public static final String CREATE_TRAIN_FARES = "createtrainfares";
}
