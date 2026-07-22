package com.footdablit2310.footlib.impl.rcc_impl;

import com.footdablit2310.footlib.api.common.rcc_api.*;
import com.footdablit2310.footlib.impl.rcc_impl.handlers.*;

public class RCCImpl implements RCCAPI {

    @Override
    public RCCResponse execute(RCCCommand command) {
        return switch (command.getCommand()) {
            case KICK -> KickHandler.handle(command);
            case BAN -> BanHandler.handle(command);
            case WHITELIST -> WhitelistHandler.handle(command);
            case BROADCAST -> BroadcastHandler.handle(command);
        };
    }
}
