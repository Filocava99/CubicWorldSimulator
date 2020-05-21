package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;

public abstract class GenericGui extends Panel {
    protected final GuiFactory guiFactory;
    private long windowId;

    protected GenericGui(int x, int y, float width, float height) {
        super(x, y, width, height);
        guiFactory = new LauncherFactory();
        guiFactory.setWidth((int) width);
        guiFactory.setHeight((int) height);
        this.setAspectRatio();
    }

    public void setAspectRatio() {
        guiFactory.setAspectRatio();
    }

    public Button createButton(String text, Vector2f position, Vector2f size) {
        return guiFactory.createButton(text, position, size);
    }

    public Label createLabel(String text, Vector2f position, Panel panelToAdd) {
        return guiFactory.createLabel(text, position, panelToAdd);
    }

    public TextInput createTextInput(String title, Panel panelToAdd) {
        return guiFactory.createTextInput(title, panelToAdd);
    }

    protected boolean isFiled(String text) {
        return !text.isEmpty();
    }

    public abstract String getTitle();

    protected long getWindowId() {
        return windowId;
    }

    protected void setWindowId(long windowId) {
        this.windowId = windowId;
    }
}
