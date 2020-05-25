package it.cubicworldsimulator.game.gui;

import it.cubicworldsimulator.game.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;

/**
 * @author Lorenzo Balzani
 */

public class LauncherFactory implements GuiFactory {

    private static final Logger logger = LogManager.getLogger(LauncherFactory.class);

    //X position where print label
    protected float X_LABEL = 20;
    //Y position where start to print
    protected float Y_START_VALUE = 15;
    //X offset between label end and input begin
    protected float X_INPUT_OFFSET = 150;
    //Y offset between rows
    protected float Y_OFFSET = 30;
    //Every time label is created this value is updated. This will be X position for input
    private float newXInput = 0;

    //Every time row is created this value is updated. This will be Y position for the next row
    private float newYLabel;

    //Screen size
    private int width;
    private int height;
    private float aspectRatio;

    //Fonts and colors
    protected float buttonFontSize = 30;
    protected float buttonHeight = 50;
    protected float buttonWidth = 80;
    protected static final String buttonFont = FontRegistry.ROBOTO_BOLD;

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public void setAspectRatio() {
        logger.debug("Height: " + this.height);
        logger.debug("Width: " + this.width);
        this.aspectRatio=(this.height*this.width)/800_000f;
        this.buttonFontSize=aspectRatio*20f;
        this.X_LABEL*=aspectRatio;
        this.Y_START_VALUE*=aspectRatio;
        this.X_INPUT_OFFSET*=aspectRatio;
        this.Y_OFFSET*=aspectRatio;
        this.newYLabel=Y_START_VALUE;
        this.buttonWidth *= aspectRatio;
        this.buttonHeight *= aspectRatio;
    }

    @Override
    public float getFontSize() {
        return this.buttonFontSize;
    }

    @Override
    public Button createButton(String text, Vector2f position, Panel panelToAdd) {
        Button button = new Button(text, position, new Vector2f(buttonWidth, buttonHeight));
        button.getStyle().setFont(buttonFont);
        button.getStyle().setFontSize(getFontSize());
        panelToAdd.add(button);
        return button;
    }

    @Override
    public Label createLabel(String messageText, Vector2f position, Panel panelToAdd) {
        Label message = new Label(messageText);
        message.getStyle().setFontSize(getFontSize());
        panelToAdd.add(message);
        message.setPosition(position);
        return message;
    }

    public Label createOptionLabel(String title, Panel panelToAdd) {
        Label label = new Label(title);
        panelToAdd.add(label);
        label.getStyle().setFontSize(getFontSize());
        label.setPosition(X_LABEL, this.newYLabel);
        newYLabel = label.getPosition().y + Y_OFFSET;
        newXInput = label.getPosition().x + label.getSize().x + X_INPUT_OFFSET;
        return label;
    }

    @Override
    public TextInput createTextInput(String title, Panel panelToAdd) {
        TextInput input = new TextInput(newXInput, this.newYLabel-Y_OFFSET-10, 130, 35);
        input.getTextState().setText(title);
        input.getStyle().setTextColor(0,0,0,1);
        input.getStyle().getBackground().setColor(ColorConstants.white());
        panelToAdd.add(input);
        return input;
    }
}
