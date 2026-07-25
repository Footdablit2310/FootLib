package com.footdablit2310.footlib.impl.rcc_impl;

import com.footdablit2310.footlib.api.common.rcc_api.*;
import com.footdablit2310.footlib.impl.rcc_impl.handlers.*;
import org.slf4j.Logger;
import com.footdablit2310.footlib.FootLib;

import java.io.InvalidObjectException;

public class RCCImpl {

    private static final Logger LOGGER = FootLib.LOGGER;

    public static RCCResponse execute(RCCCommand command) {
        try {

            return switch (command.getCommand()) {
                case KICK -> KickHandler.handle(command);
                case BAN -> BanHandler.handle(command);
                case WHITELIST -> WhitelistHandler.handle(command);
                case BROADCAST -> BroadcastHandler.handle(command);
            };
        } catch (InvalidObjectException invalidObjectException) {
            LOGGER.warn("Warning Skipping RCC process.{}", System.lineSeparator(), invalidObjectException);
            Data SubCommandExecptionResponseData = new Data();
            SubCommandExecptionResponseData.put("SubCommand Parent", command.getCommand().toString());
            return new RCCResponse("error", "Invalid SubCommand", SubCommandExecptionResponseData);
        }
    }
}
