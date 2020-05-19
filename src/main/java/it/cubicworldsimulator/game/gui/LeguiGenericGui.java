package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;

public abstract class LeguiGenericGui extends Panel implements GenericGui {
    private final GuiFactory guiFactory;
    protected long windowId;

    protected LeguiGenericGui(int x, int y, float width, float height) {
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

    public Label createLabel(String messageText, Vector2f position) {
        return guiFactory.createLabel(messageText, position);
    }

    public Label createOptionLabel(String title, Panel panelToAdd) {
        return guiFactory.createOptionLabel(title, panelToAdd);
    }

    public TextInput createTextInput(String title, Panel panelToAdd) {
        return guiFactory.createTextInput(title, panelToAdd);
    }

    protected boolean isFiled(String text) {
        return !text.isEmpty();
    }

    protected void setWindow(long windowId) {
        this.windowId = windowId;
    }
}
