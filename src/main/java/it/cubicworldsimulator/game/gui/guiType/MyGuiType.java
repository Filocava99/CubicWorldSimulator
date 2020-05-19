package it.cubicworldsimulator.game.gui.guiType;

import it.cubicworldsimulator.game.gui.Gui;

import java.util.Optional;

public class MyGuiType implements GuiType {

    private final String className;

    public MyGuiType(String className) {
        this.className=className;
    }

    /**
     * @return new instance of a class that extends gui
     */
    @Override
    public Optional<Gui> instanceNewGui() {
        try {
            Class myGui = Class.forName(className);
            if (Gui.class.isAssignableFrom(myGui)) {
                return Optional.of((Gui) myGui.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
