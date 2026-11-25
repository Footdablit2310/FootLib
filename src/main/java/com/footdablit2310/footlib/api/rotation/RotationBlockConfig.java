package com.footdablit2310.footlib.api.rotation;

import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

/** Default settings plus partial BlockState-specific overrides. */
public final class RotationBlockConfig {
    private final RotationSettings defaults;
    private final Map<Map<String, String>, RotationSettings> blockStates;

    public RotationBlockConfig(RotationSettings defaults, Map<Map<String, String>, RotationSettings> blockStates) {
        this.defaults = defaults;
        this.blockStates = Map.copyOf(blockStates);
    }

    public RotationSettings defaults() { return defaults; }
    public Map<Map<String, String>, RotationSettings> blockStates() { return blockStates; }

    /** Most-specific matching property subset wins; unmatched values inherit from default. */
    public RotationSettings resolve(BlockState state) {
        RotationSettings best = defaults;
        int bestSpecificity = -1;
        for (Map.Entry<Map<String, String>, RotationSettings> entry : blockStates.entrySet()) {
            Map<String, String> matcher = entry.getKey();
            if (matcher.size() > bestSpecificity && matches(state, matcher)) {
                best = entry.getValue();
                bestSpecificity = matcher.size();
            } else if (matcher.size() == bestSpecificity && bestSpecificity >= 0 && matches(state, matcher)) {
                throw new IllegalStateException("Ambiguous FootLib rotation blockstate overrides for " + state);
            }
        }
        return best;
    }

    private static boolean matches(BlockState state, Map<String, String> matcher) {
        for (Map.Entry<String, String> expected : matcher.entrySet()) {
            String actual = getStateProperty(state, expected.getKey());
            if (actual == null || !actual.equals(expected.getValue())) return false;
        }
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String getStateProperty(BlockState state, String propertyName) {
        for (net.minecraft.world.level.block.state.properties.Property property : state.getProperties()) {
            if (property.getName().equals(propertyName)) {
                Comparable value = state.getValue(property);
                return property.getName(value).toLowerCase(java.util.Locale.ROOT);
            }
        }
        return null;
    }
}
