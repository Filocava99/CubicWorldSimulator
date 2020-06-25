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
    /**
     * @return height of the window
     */
    int getHeight();

    /**
     * Set the window height
     * @param height of the window
     */
    void setHeight(int height);

    /**
     * @return width of the window
     */
    int getWidth();

    /**
     * Set the window width
     * @param width
     */
    void setWidth(int width);

    /**
     * Set the window aspect ratio
     */
    void setAspectRatio();

    /**
     * @return the font size of the items
     */
    float getFontSize();

    /**
     * Creates a button
     * @param text of the button
     * @param position in which the button has to be added
     * @param panelToAdd in which the button has to be added
     * @return the Button
     */
    Button createButton(String text, Vector2f position, Panel panelToAdd);

    /**
     * Creates a label
     * @param text of the label
     * @param position in which the label has to be added
     * @param panelToAdd in which the label has to be added
     * @return the Label
     */
    Label createLabel(String text, Vector2f position, Panel panelToAdd);

    /**
     * Creates a text input
     * @param title of the label
     * @param panelToAdd n which the text input has to be added
     * @return the TextInput
     */
    TextInput createTextInput(String title, Panel panelToAdd);
}
