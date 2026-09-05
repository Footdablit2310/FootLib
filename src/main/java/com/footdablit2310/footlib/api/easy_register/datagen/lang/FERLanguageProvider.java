package com.footdablit2310.footlib.api.easy_register.datagen.lang;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class FERLanguageProvider extends LanguageProvider {

    private final FootEasyRegisterSystem reg;

    public FERLanguageProvider(PackOutput output, String modid, FootEasyRegisterSystem reg) {
        super(output, modid, "en_us");
        this.reg = reg;
    }

    @Override
    protected void addTranslations() {

        // Add creative tab names
        reg.getCreativeTabLang().forEach(this::add);

        // Add other mod translations...
    }
}
