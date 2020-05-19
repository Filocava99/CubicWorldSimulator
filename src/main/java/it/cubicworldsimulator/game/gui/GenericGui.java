package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;

/**
 * It models a generic Gui. It has check methods and element creation methods.
 */
public abstract class GenericGui extends Panel implements GuiUtility {
    protected long window;
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

    protected GenericGui(int x, int y, float width, float height) {
        super(x, y, width, height);
        this.width=(int)width;
        this.height=(int)height;
        this.setAspectRatio();
    }

    private void setAspectRatio() {
        this.aspectRatio=this.width/ (float) this.height;
        this.X_LABEL*=aspectRatio;
        this.Y_START_VALUE*=aspectRatio;
        this.X_INPUT_OFFSET*=aspectRatio;
        this.Y_OFFSET*=aspectRatio;
        this.newYLabel=Y_START_VALUE;
    }

    public void setWindow (long window) {
        this.window=window;
    }

    protected Button createButton(String text, Vector2f position, Vector2f size) {
        size.x*=aspectRatio;
        size.y*=aspectRatio;
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(30f*aspectRatio);
        button.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
        return button;
    }

    protected Label createMessage(String messageText, Vector2f position) {
        Label message = new Label(messageText);
        message.setPosition(position);
        return message;
    }

    protected Label createOptionLabel(String title, Panel panelToAdd) {
        Label label = new Label(title);
        panelToAdd.add(label);
        label.setPosition(X_LABEL, this.newYLabel);
        newYLabel=label.getPosition().y+Y_OFFSET;
        newXInput=label.getPosition().x+label.getSize().x+X_INPUT_OFFSET;
        return label;
    }

    protected TextInput createOptionInput(String title, Panel panelToAdd) {
        TextInput input = new TextInput(newXInput, this.newYLabel-Y_OFFSET-10, 130, 35);
        input.getTextState().setText(title);
        input.getStyle().setTextColor(0,0,0,1);
        input.getStyle().getBackground().setColor(ColorConstants.white());
        panelToAdd.add(input);
        return input;
    }
}
