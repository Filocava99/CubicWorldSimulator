package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;

/**
 * It models a generic Gui factory. Can be used to generate different type of gui
 */
public interface GuiFactory {
    int getWidth();
    void setWidth(float width);
    int getHeight();
    void setHeight(float height);
    void setAspectRatio();
    Button createButton(String text, Vector2f position, Vector2f size);
    Label createLabel(String messageText, Vector2f position, Panel panelToAdd);
    TextInput createTextInput(String title, Panel panelToAdd);
}
