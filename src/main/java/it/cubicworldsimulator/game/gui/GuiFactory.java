package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;

/**
 * It models a generic Gui factory. Can be used to generate different type of gui
 * @autor Lorenzo Balzani
 */
public interface GuiFactory {
    int getHeight();
    void setHeight(int height);
    int getWidth();
    void setWidth(int width);
    void setAspectRatio();
    float getFontSize();
    Button createButton(String text, Vector2f position, Panel panelToAdd);
    Label createLabel(String messageText, Vector2f position, Panel panelToAdd);
    TextInput createTextInput(String title, Panel panelToAdd);
}
