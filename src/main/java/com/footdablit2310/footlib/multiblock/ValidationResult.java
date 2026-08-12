package com.footdablit2310.footlib.multiblock;

import net.minecraft.network.chat.Component;

public record ValidationResult(boolean success, Component message) {
    public static ValidationResult ok(String key, Object... args) {
        return new ValidationResult(true, Component.translatable(key, args));
    }
    public static ValidationResult fail(String key, Object... args) {
        return new ValidationResult(false, Component.translatable(key, args));
    }
}