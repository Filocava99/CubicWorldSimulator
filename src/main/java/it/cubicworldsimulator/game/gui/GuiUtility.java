package it.cubicworldsimulator.game.gui;

import java.util.Optional;

public interface GuiUtility {
    default boolean isBoolean (String text) {
        return text.equalsIgnoreCase("false") || text.equalsIgnoreCase("true");
    }

    default boolean isFiled (String text) {
        return !text.isEmpty();
    }

    default Optional<Boolean> parseBoolean (String text) {
        if (isBoolean(text)) {
            return Optional.of(Boolean.parseBoolean(text));
        }
        return Optional.empty();
    }
}
