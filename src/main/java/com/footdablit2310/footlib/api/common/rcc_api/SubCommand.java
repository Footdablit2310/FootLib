package com.footdablit2310.footlib.api.common.rcc_api;

public record SubCommand(Command parent, String name) {
    public Boolean isValid() {
        if (this.parent == Command.BAN || this.parent == Command.WHITELIST) {
            return this.name.equalsIgnoreCase("ADD") || this.name.equalsIgnoreCase("REMOVE");
        } else {
            return this.name.isBlank();
        }
    }
}
