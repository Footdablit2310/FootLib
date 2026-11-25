package com.footdablit2310.footlib.api.rotation;

import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;
import java.util.Map;

/** Default settings plus partial BlockState-specific overrides. */
public final class RotationBlockConfig {
    private final RotationSettings defaults;
    private final Map<Map<String, String>, RotationSettingsOverride> blockStates;

    public RotationBlockConfig(RotationSettings defaults,
            Map<Map<String, String>, RotationSettingsOverride> blockStates) {
        this.defaults = defaults;
        this.blockStates = Map.copyOf(blockStates);
    }

    public RotationSettings defaults() { return defaults; }
    public Map<Map<String, String>, RotationSettingsOverride> blockStates() { return blockStates; }

    /** Apply matching overrides from least to most specific. */
    public RotationSettings resolve(BlockState state) {
        RotationSettings resolved = defaults;
        var matching = blockStates.entrySet().stream()
                .filter(entry -> matches(state, entry.getKey()))
                .sorted(Comparator.comparingInt(entry -> specificity(entry.getKey())))
                .toList();

        int previousSpecificity = -1;
        for (var entry : matching) {
            int specificity = specificity(entry.getKey());
            if (specificity == previousSpecificity) {
                throw new IllegalStateException("Ambiguous FootLib rotation blockstate overrides for " + state);
            }
            resolved = entry.getValue().apply(resolved);
            previousSpecificity = specificity;
        }
        return resolved;
    }

    private static int specificity(Map<String, String> matcher) {
        int score = 0;
        for (String value : matcher.values()) {
            if (value.equals("*")) continue;
            score += value.startsWith("!") ? 1 : 2;
        }
        return score;
    }

    public boolean matchesAnyState(Iterable<BlockState> states, Map<String, String> matcher) {
        for (BlockState state : states) if (matches(state, matcher)) return true;
        return false;
    }

    private static boolean matches(BlockState state, Map<String, String> matcher) {
        for (Map.Entry<String, String> expected : matcher.entrySet()) {
            String actual = getStateProperty(state, expected.getKey());
            if (actual == null) return false;
            String wanted = expected.getValue();
            if (wanted.equals("*")) continue;
            if (wanted.startsWith("!")) {
                if (actual.equals(wanted.substring(1))) return false;
            } else if (!actual.equals(wanted)) {
                return false;
            }
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
