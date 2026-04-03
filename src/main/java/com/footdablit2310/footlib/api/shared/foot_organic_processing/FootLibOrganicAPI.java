package com.footdablit2310.footlib.api.shared.foot_organic_processing;

public final class FootLibOrganicAPI {

    private static IFootOrganicAccess access;

    public static void register(IFootOrganicAccess impl) {
        access = impl;
    }

    public static IFootOrganicAccess get() {
        return access;
    }
}
