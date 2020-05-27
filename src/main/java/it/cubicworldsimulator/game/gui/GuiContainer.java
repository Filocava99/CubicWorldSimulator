package it.cubicworldsimulator.game.gui;

import java.util.List;

/**
 * It creates a logic container for all Guis to render
 */
public interface GuiContainer {
    void init(List<GenericGui> guiList);
}
