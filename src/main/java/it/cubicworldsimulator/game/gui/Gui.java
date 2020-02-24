package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;

import java.util.Optional;

public abstract class Gui extends Panel {
    protected long window;
    //X position where print label
    protected final static float X_LABEL = 20;
    //Y position where start to print
    protected final static float Y_START_VALUE = 15;
    //X offset between label end and input begin
    protected final static float X_INPUT_OFFSET = 200;
    //Y offset between rows
    protected final static float Y_OFFSET = 50;
    //Every time label is created this value is updated. This will be X position for input
    private float newXInput = 0;

    //Every time row is created this value is updated. This will be Y position for the next row
    private float newYLabel=Y_START_VALUE;

    protected Gui(int x, int y, float width, float height) {
        super(x, y, width, height);
    }

    public void setWindow (long window) {
        this.window=window;
    }

    protected boolean isNumeric(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected boolean isBoolean (String text) {
        return text.equalsIgnoreCase("false") || text.equalsIgnoreCase("true");
    }

    protected boolean isFiled (String text) {
        return !text.isEmpty();
    }

    protected Optional<Boolean> parseBoolean (String text) {
        if (isBoolean(text)) {
            return Optional.of(Boolean.parseBoolean(text));
        }
        return Optional.empty();
    }

    protected Button createButton(String text, Vector2f position, Vector2f size) {
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(30f);
        button.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
        return button;
    }

    protected Label createMessage(String messageText, Vector2f position) {
        Label message = new Label(messageText);
        message.setPosition(position);
        message.getStyle().setFontSize(20f);
        return message;
    }

    protected Label createOptionLabel(String title, Panel panelToAdd) {
        Label label = new Label(title);
        label.getStyle().setFontSize(50f);
        panelToAdd.add(label);
        label.setPosition(X_LABEL, this.newYLabel);
        newYLabel=label.getPosition().y+Y_OFFSET;
        newXInput=label.getPosition().x+label.getSize().x+X_INPUT_OFFSET;
        return label;
    }

    protected TextInput createOptionInput(String title, Panel panelToAdd) {
        TextInput input = new TextInput(newXInput, this.newYLabel-Y_OFFSET-10, 130, 35);
        input.getTextState().setText(title);
        input.getStyle().setFontSize(20f);
        input.getStyle().setTextColor(0,0,0,1);
        input.getStyle().getBackground().setColor(ColorConstants.white());
        panelToAdd.add(input);
        input.getStyle().setFontSize(30f);
        return input;
    }
}
