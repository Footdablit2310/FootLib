package com.footdablit2310.footlib.api.common.rcc_api;

public class RCCCommand {
    private final Command command;
    private final SubCommand subcommand;
    private final Data data;

    public RCCCommand(Command command, SubCommand subcommand, Data data) {
        this.command = command;
        this.subcommand = subcommand;
        this.data = data;
    }

    public Command getCommand() { return command; }
    public SubCommand getSubcommand() { return subcommand; }
    public Data getData() { return data; }
}
