package com.footdablit2310.footlib.api.common;

import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import java.util.List;

public final class ModPresence {

    private ModPresence() {}

    /**
     * Universal mod presence checker.
     * Usage:
     *   ModPresence.isInstalled("create")
     *   ModPresence.isInstalled("jei")
     *   ModPresence.isInstalled("footorganicprocessing")
     */
    public static boolean isInstalled(String modId) {
        return ModList.get().isLoaded(modId);
    }
    /**
     * This gets all the mods id's from the mods that are installed and returns them as a List<String>
     */
    public static List<String> ReturnAllInstalledModIdsAsString() {
        for (ModContainer ModData : ModList.get().getSortedMods()) {
            ReturnAllInstalledModIdsAsString().add(ModData.getModId());
        };
        return ReturnAllInstalledModIdsAsString();
    };
    /**
     * This method gives you a sorted List<ModContainer> with all of the installed mods available 
     * NOTE: This does not process any data it just returns a List<ModContainer>
     * NOTE: Uses ModContainer for raw data with the import net.neoforged.fml.ModContainer
     * @return List<ModContainer>
     */
    public static List<ModContainer> ReturnAllInstalledModContainerAsList() {
        return ModList.get().getSortedMods();
    }

    //Others mods mod id as constants
    public static final String CREATE = "create";
    public static final String JEI = "jei";
    public static final String EMI = "emi";
    public static final String CREATE_RAILWAYS_NAVIGATOR = "createrailwaysnavigator";

    //My own mods mod id as constants
    public static final String FOOTLIB = "footlib";
    public static final String FOOT_DEVICES = "footdevices";
    public static final String FOOT_ELECTRICITY = "footelectricity";
    public static final String FOOT_ECONOMY = "footeconomy";
    public static final String FOOT_ORGNIC_PROCESSING = "footorganicprocessing";
    public static final String CREATE_TRAIN_FARES = "createtrainfares";
}
