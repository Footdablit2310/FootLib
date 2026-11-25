package com.footdablit2310.footlib.api.rotation;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Comparator;
import java.util.Map;

/** Default settings plus partial BlockState-specific overrides. */
public final class RotationBlockConfig {
    private final RotationSettings defaults;
    private final Map<Map<String, String>, RotationSettingsOverride> blockStates;

    public RotationBlockConfig(RotationSettings defaults, Map<Map<String, String>, RotationSettingsOverride> blockStates) {
        this.defaults = defaults;
        this.blockStates = Map.copyOf(blockStates);
    }

    public RotationSettings defaults() { return defaults; }
    public Map<Map<String, String>, RotationSettingsOverride> blockStates() { return blockStates; }

    /** Applies matching entries from least to most specific, rejecting equal-specificity matches. */
    public RotationSettings resolve(BlockState state) {
        var matching = blockStates.entrySet().stream().filter(e -> matches(state, e.getKey()))
                .sorted(Comparator.comparing(RotationBlockConfig::specificity, RotationBlockConfig::compareSpecificity))
                .toList();
        RotationSettings resolved = defaults;
        Specificity previous = null;
        for (var entry : matching) {
            Specificity current = specificity(entry.getKey());
            if (previous != null && compareSpecificity(previous, current) == 0)
                throw new IllegalStateException("Ambiguous FootLib rotation blockstate overrides for " + state);
            resolved = entry.getValue().apply(resolved);
            previous = current;
        }
        return resolved;
    }

    private record Specificity(int propertyCount, int exactCount, int exclusionCount, int wildcardCount) {}

    private static Specificity specificity(Map<String, String> matcher) {
        int exact = 0, exclusion = 0, wildcard = 0;
        for (String value : matcher.values()) {
            if (value.equals("*")) wildcard++;
            else if (value.startsWith("!")) exclusion++;
            else exact++;
        }
        return new Specificity(matcher.size(), exact, exclusion, wildcard);
    }

    private static int compareSpecificity(Specificity a, Specificity b) {
        int c = Integer.compare(a.propertyCount, b.propertyCount);
        if (c != 0) return c;
        c = Integer.compare(a.exactCount, b.exactCount);
        if (c != 0) return c;
        c = Integer.compare(a.exclusionCount, b.exclusionCount);
        if (c != 0) return c;
        return Integer.compare(b.wildcardCount, a.wildcardCount);
    }

    public boolean matchesAnyState(Iterable<BlockState> states, Map<String, String> matcher) {
        for (BlockState state : states) if (matches(state, matcher)) return true;
        return false;
    }

    private static boolean matches(BlockState state, Map<String, String> matcher) {
        for (var expected : matcher.entrySet()) {
            String actual = getStateProperty(state, expected.getKey());
            if (actual == null) return false;
            String wanted = expected.getValue();
            if (wanted.equals("*")) continue;
            if (wanted.startsWith("!")) {
                if (actual.equals(wanted.substring(1))) return false;
            } else if (!actual.equals(wanted)) return false;
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
