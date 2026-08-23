package com.footdablit2310.footlib.api.common.basic;

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
    public static List<ModContainer> getAllInstalledModContainerAsList() {
        return ModList.get().getSortedMods();
    }
    public static List<String> getAllInstalledModsAsModIdList() {
        return ModList.get().getSortedMods()
                .stream()
                .map(ModContainer::getModId)
                .toList();
    }

    // JEI and EMI
    public static final String JEI = "jei";
    public static final String EMI = "emi";
    // Create Mod
    public static final String CREATE = "create";
    public static final String PONDER = "ponder";
    public static final String FLYWHEEL = "flywheel";
    // CRN
    public static final String CREATE_RAILWAYS_NAVIGATOR = "createrailwaysnavigator";
    // Sable
    public static final String SABLE_COMPANION = "sablecompanion";
    public static final String SABLE = "sable";

    // Foot mods
    public static final String FOOTLIB = "footlib";
    public static final String FOOT_DEVICES = "footdevices";
    public static final String FOOT_ELECTRICITY = "footelectricity";
    public static final String FOOT_ECONOMY = "footeconomy";
    public static final String FOOT_ORGANIC_PROCESSING = "footorganicprocessing";
    public static final String CREATE_TRAIN_FARES = "createtrainfares";

}
