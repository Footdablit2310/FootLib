package com.footdablit2310.footlib.api.common.rcc_api;

public class SubCommand {
    private final Command parent;
    private final String name;

    public SubCommand(Command parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public Command getParent() { return parent; }
    public String getName() { return name; }
}
