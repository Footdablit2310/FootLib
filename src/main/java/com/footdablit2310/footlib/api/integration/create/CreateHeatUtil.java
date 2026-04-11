package com.footdablit2310.footlib.api.integration.create;

public final class CreateHeatUtil {

    private CreateHeatUtil() {}

    /**
     * Returns Create-compatible heat tiers.
     * ultraheated is intentionally NOT mapped.
     */
    public static String mapToCreate(String heat) {
        return switch (heat) {
            case "heated" -> "heated";
            case "superheated" -> "superheated";
            default -> null; // ultraheated or unknown → not Create-compatible
        };
    }

    public static boolean isCreateCompatible(String heat) {
        return heat.equals("heated") || heat.equals("superheated");
    }
}
