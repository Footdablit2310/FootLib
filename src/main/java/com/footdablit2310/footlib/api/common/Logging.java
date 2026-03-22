package com.footdablit2310.footlib.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Logging {

    private Logging() {}

    public static Logger get(String modId) {
        return LoggerFactory.getLogger(modId);
    }

    public static Logger footlib() {
        return get("footlib");
    }
}