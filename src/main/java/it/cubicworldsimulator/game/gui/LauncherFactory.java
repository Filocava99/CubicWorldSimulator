package it.cubicworldsimulator.game.gui;

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

    //X position where print label
    protected float X_LABEL = 20;
    //Y position where start to print
    protected float Y_START_VALUE = 15;
    //X offset between label end and input begin
    protected float X_INPUT_OFFSET = 100;
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
    protected static final float buttonFontSize = 30;
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
        this.aspectRatio=this.width/ (float) this.height;
        this.X_LABEL*=aspectRatio;
        this.Y_START_VALUE*=aspectRatio;
        this.X_INPUT_OFFSET*=aspectRatio;
        this.Y_OFFSET*=aspectRatio;
        this.newYLabel=Y_START_VALUE;
    }



    @Override
    public Button createButton(String text, Vector2f position, Vector2f size) {
        size.x*=aspectRatio;
        size.y*=aspectRatio;
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(buttonFontSize*aspectRatio);
        button.getStyle().setFont(buttonFont);
        return button;
    }

    @Override
    public Label createLabel(String messageText, Vector2f position, Panel panelToAdd) {
        Label message = new Label(messageText);
        panelToAdd.add(message);
        message.setPosition(position);
        return message;
    }

    public Label createOptionLabel(String title, Panel panelToAdd) {
        Label label = new Label(title);
        panelToAdd.add(label);
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
