package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;

import static org.liquidengine.legui.component.optional.align.HorizontalAlign.CENTER;
import static org.liquidengine.legui.component.optional.align.VerticalAlign.MIDDLE;
import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static org.liquidengine.legui.style.color.ColorUtil.fromInt;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;

public class LauncherGui extends GuiType {


    private final static float X_LABEL = 5;
    private final static float Y_START_VALUE = 15;
    private final static float X_INPUT_OFFSET = 150;
    private final static float Y_OFFSET = 50;
    private boolean fullscreen=true;
    private boolean vSync=true;
    private boolean debug=false;
    private float newYLabel=Y_START_VALUE;
    private long window;

    public LauncherGui(Vector2i size) {
        super(0,0,size.x,size.y);
        this.createGui(size);
    }

    private void createGui(Vector2i size) {
        Panel settings = new Panel(0, 200, size.x, 400);
        Label settingsLabel = this.createOptionLabel("Settings", settings);

        Label vSync = this.createOptionLabel("vSync", settings);
        TextInput vSyncInput = this.createOptionInput("true", settings);

        Label debugLabel = this.createOptionLabel("Debug", settings);
        TextInput debugInput = this.createOptionInput("false", settings);

        Label fullscreenLabel = this.createOptionLabel("Fullscreen", settings);
        TextInput fullScreenInput = this.createOptionInput("true", settings);

        Label widthLabel = this.createOptionLabel("Width", settings);
        TextInput widthInput = this.createOptionInput("fs", settings);
        widthInput.setEditable(false);

        Label heightLabel = this.createOptionLabel("Height", settings);
        TextInput heightInput = this.createOptionInput("fs", settings);
        heightInput.setEditable(false);


        Label widthMessage = this.createMessage("Value not correct!",
                new Vector2f(widthInput.getPosition().x + widthInput.getSize().x + 20, widthLabel.getPosition().y));
        widthInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumericOrFs(event.getNewValue())) {
                settings.add(widthMessage);
                Themes.getDefaultTheme().applyAll(widthMessage);
            } else {
                settings.remove(widthMessage);
            }
        });
        Label heightMessage = this.createMessage("Value not correct!",
                new Vector2f(heightInput.getPosition().x + heightInput.getSize().x + 20, heightLabel.getPosition().y));
        heightInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumericOrFs(event.getNewValue())) {
                settings.add(heightMessage);
                Themes.getDefaultTheme().applyAll(heightMessage);
            } else {
                settings.remove(heightMessage);
            }
        });
        Label fullScreenMessage = this.createMessage("Please choose window size",
                new Vector2f(fullScreenInput.getPosition().x + fullScreenInput.getSize().x + 20, fullscreenLabel.getPosition().y));
        fullScreenInput.addTextInputContentChangeEventListener(event -> {
            if (event.getNewValue().equalsIgnoreCase("false")) {
                settings.add(fullScreenMessage);
                Themes.getDefaultTheme().applyAll(fullScreenMessage);
                widthInput.setEditable(true);
                heightInput.setEditable(true);
                widthInput.getTextState().setText("1920");
                heightInput.getTextState().setText("1080");
            } else {
                settings.remove(fullScreenMessage);
                widthInput.setEditable(false);
                heightInput.setEditable(false);
                widthInput.getTextState().setText("fs");
                heightInput.getTextState().setText("fs");
                settings.remove(widthMessage);
                settings.remove(heightMessage);
            }
        });
        Button launchGame = this.createButton("Start game", new Vector2f(290, 50), new Vector2f(120, 90));
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction() && isNumericOrFs(widthInput.getTextState().getText()) &&
                    isNumericOrFs(heightInput.getTextState().getText())) {
                if (checkStringIsBoolean(fullScreenInput.getTextState().getText())) {
                    this.fullscreen = Boolean.parseBoolean(fullScreenInput.getTextState().getText());
                }
                if (checkStringIsBoolean(vSyncInput.getTextState().getText())) {
                    this.vSync = Boolean.parseBoolean(vSyncInput.getTextState().getText());
                }
                if (checkStringIsBoolean(debugInput.getTextState().getText())) {
                    this.debug = Boolean.parseBoolean(debugInput.getTextState().getText());
                }
                try {
                    GameEngine gameEngine = new GameEngine("CubicWorldSimulator",
                            this.vSync, new Game(), this.debug);
                    glfwDestroyWindow(window);
                    gameEngine.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.add(launchGame);
        this.add(settings);
        this.switchTheme();
        Themes.getDefaultTheme().applyAll(this);
        settingsLabel.getStyle().setFontSize(40f);
        launchGame.getStyle().setFontSize(50f);
    }

    private boolean isNumericOrFs(String string) {
        if (string == null) {
            return false;
        }
        if (string.equals("fs") || string.equals("FS")) {
            return true;
        }
        try {
            int value = Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private Button createButton(String text, Vector2f position, Vector2f size) {
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(30f);
        button.getStyle().setHorizontalAlign(CENTER);
        button.getStyle().setVerticalAlign(MIDDLE);
        button.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
        return button;
    }

    private Label createMessage(String messageText, Vector2f position) {
        Label message = new Label(messageText);
        message.setPosition(position);
        message.getStyle().setFontSize(20f);
        return message;
    }

    private boolean checkStringIsBoolean (String text) {
        return text.equalsIgnoreCase("false") || text.equalsIgnoreCase("true");
    }

    private Label createOptionLabel(String title, Panel panelToAdd) {
        Label label = new Label(title);
        label.getStyle().setFontSize(50f);
        panelToAdd.add(label);
        label.setPosition(X_LABEL, this.newYLabel);
        newYLabel=label.getPosition().y+Y_OFFSET;
        return label;
    }

    private TextInput createOptionInput(String title, Panel panelToAdd) {
        TextInput input = new TextInput(X_INPUT_OFFSET, this.newYLabel-Y_OFFSET-10, 70, 35);
        input.getTextState().setText(title);
        input.getStyle().setFontSize(20f);
        input.getStyle().setTextColor(0,0,0,1);
        input.getStyle().setHorizontalAlign(CENTER);
        input.getStyle().setVerticalAlign(MIDDLE);
        input.getStyle().getBackground().setColor(ColorConstants.white());
        panelToAdd.add(input);
        input.getStyle().setFontSize(30f);
        return input;
    }

    private void switchTheme() {
        Themes.setDefaultTheme(new FlatColoredTheme(
                fromInt(44, 62, 80, 1), // backgroundColor
                fromInt(127, 140, 141, 1), // borderColor
                fromInt(127, 140, 141, 1), // sliderColor
                fromInt(2, 119, 189, 1), // strokeColor
                fromInt(39, 174, 96, 1), // allowColor
                fromInt(192, 57, 43, 1), // denyColor
                fromInt(0, 0, 0, 1f),  // shadowColor
                ColorConstants.white(),
                FontRegistry.DEFAULT,
                30f
        ));

    }

    @Override
    public void setWindow (long window) {
        this.window=window;
    }
}