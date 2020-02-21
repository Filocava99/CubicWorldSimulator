package it.cubicworldsimulator.game;

import org.liquidengine.legui.component.Panel;

public abstract class GuiType extends Panel {
    public GuiType(int x, int y, float width, float height) {
        super(x, y, width, height);
    }
    public abstract void setWindow (long window);
}
