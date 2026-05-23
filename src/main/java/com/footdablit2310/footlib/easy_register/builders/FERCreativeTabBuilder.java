package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class FERCreativeTabBuilder {

    private final FootEasyRegisterSystem reg;
    private final String name;

    private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
    private final List<Supplier<ItemStack>> entries = new ArrayList<>();

    public FERCreativeTabBuilder(FootEasyRegisterSystem reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    public FERCreativeTabBuilder icon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public FERCreativeTabBuilder add(Supplier<ItemStack> stack) {
        entries.add(stack);
        return this;
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> register() {
        return reg.tabs.register(name, () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + reg.getModId() + "." + name))
                .icon(icon)
                .displayItems((params, output) -> entries.forEach(s -> output.accept(s.get())))
                .build()
        );
    }
}
