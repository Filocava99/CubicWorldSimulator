package it.cubicworldsimulator.game.gui.guiType;

import it.cubicworldsimulator.game.gui.Gui;

import java.util.Optional;

public interface GuiType {
    Optional<Gui> instanceNewGui();
}
