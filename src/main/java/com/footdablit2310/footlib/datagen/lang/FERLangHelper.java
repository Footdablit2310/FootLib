package com.footdablit2310.footlib.datagen.lang;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

public final class FERLangHelper {

    private FERLangHelper() {}

    public static String defaultName(ResourceLocation id) {
        String path = id.getPath().replace('_', ' ');
        String[] parts = path.split(" ");
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) b.append(' ');
            if (!parts[i].isEmpty()) {
                b.append(Character.toUpperCase(parts[i].charAt(0)));
                if (parts[i].length() > 1) {
                    b.append(parts[i].substring(1));
                }
            }
        }
        return b.toString();
    }

    public static void addBlock(net.minecraft.data.PackOutput output,
                                net.minecraft.data.DataProvider provider,
                                net.minecraft.data.advancements.AdvancementSubProvider lang,
                                Block block, String name) {
        // you’ll usually just use LanguageProvider in the mod,
        // this helper is mostly for name generation
    }

    public static void addItem(net.minecraft.data.PackOutput output,
                               net.minecraft.data.DataProvider provider,
                               net.minecraft.data.advancements.AdvancementSubProvider lang,
                               Item item, String name) {
        // same note as above
    }
}
