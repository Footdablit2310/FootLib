package com.footdablit2310.footlib.datagen.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

public abstract class FERLanguageProvider extends LanguageProvider {

    public FERLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    /** Auto-generate English names from registry IDs */
    protected String autoName(ResourceLocation id) {
        String path = id.getPath().replace('_', ' ');
        String[] parts = path.split(" ");
        StringBuilder b = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            b.append(Character.toUpperCase(p.charAt(0)))
             .append(p.substring(1))
             .append(" ");
        }
        return b.toString().trim();
    }

    protected void addBlockAuto(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        add(block, autoName(id));
    }

    protected void addItemAuto(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        add(item, autoName(id));
    }
}
