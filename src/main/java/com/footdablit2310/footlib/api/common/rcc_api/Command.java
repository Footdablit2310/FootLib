package com.footdablit2310.footlib.api.common.rcc_api;

public enum Command {
    KICK,
    BAN,
    WHITELIST,
    BROADCAST;

    public static Command of(String name) {
        return Command.valueOf(name.toUpperCase());
    }
}
