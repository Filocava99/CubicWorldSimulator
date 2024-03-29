package it.cubicworldsimulator.game.gui;

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
 * The implementation of a generic GuiFactory
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
    //Input
    private float inputWidth = 130;
    private float inputHeight = 35;
    private float yInputOffset;
    //Buttons
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

    @Override
    public void setAspectRatio() {
        if (this.width >= 3840 && height >= 2160) {
            set4K();
        } else if (width >= 1920 && width <3840 && height >= 1080 && height < 2160){
            setFullHD();
        } else if (width >= 1440 && width <1920 && height >= 900 && height < 1080){
           setOtherResolutions();
        }
        this.buttonFontSize= aspectRatio *20f;
        this.X_LABEL*= aspectRatio;
        this.Y_START_VALUE*= aspectRatio;
        this.X_INPUT_OFFSET*= aspectRatio;
        this.newYLabel=Y_START_VALUE;
        this.buttonWidth *= aspectRatio;
        this.buttonHeight *= aspectRatio;
        this.inputWidth *= aspectRatio;
        this.inputHeight = inputHeight * aspectRatio - 15f;
    }

    /**
     * Set items scaling on 4K monitors.
     * @author Lorenzo Balzani
     */
    private void set4K() {
        logger.debug("4K screen resolution detected.");
        aspectRatio = (height * width) / 2_000_000f;
        Y_OFFSET = (Y_OFFSET * aspectRatio) + 10f;
        yInputOffset = 50;
    }

    /**
     * Set items scaling on FULL HD monitors.
     * @author Lorenzo Balzani
     */
    private void setFullHD(){
        logger.debug("FullHD screen resolution detected.");
        aspectRatio = (height * width) / 800_000f;
        Y_OFFSET *= aspectRatio + 0f;
        yInputOffset = 20;
    }

    /**
     * Set items scaling on other monitors.
     * @author Lorenzo Balzani
     */
    private void setOtherResolutions() {
        logger.debug(width + "x" + height + " screen resolution detected.");
        aspectRatio = (height * width) / 800_000f;
        Y_OFFSET = (Y_OFFSET * aspectRatio) + 5f;
        yInputOffset = 10;
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

    /**
     * It create a specific label. It's left to a TextInput
     * @author Lorenzo Balzani
     */
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
        TextInput input = new TextInput(newXInput, this.newYLabel-Y_OFFSET-yInputOffset, inputWidth, inputHeight);
        input.getTextState().setText(title);
        input.getStyle().setTextColor(0,0,0,1);
        input.getStyle().getBackground().setColor(ColorConstants.white());
        panelToAdd.add(input);
        return input;
    }
}
