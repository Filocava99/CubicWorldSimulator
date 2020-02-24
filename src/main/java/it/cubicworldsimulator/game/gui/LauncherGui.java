package it.cubicworldsimulator.game.gui;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.game.Game;
import it.cubicworldsimulator.game.GuiFactory;
import it.cubicworldsimulator.game.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.liquidengine.legui.component.*;
import org.liquidengine.legui.component.event.selectbox.SelectBoxChangeSelectionEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;

import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static org.liquidengine.legui.style.color.ColorUtil.fromInt;
import static org.lwjgl.glfw.GLFW.*;

public class LauncherGui extends Gui {
    private static final Logger logger = LogManager.getLogger(LauncherGui.class);
    GameEngine gameEngine;

    //Flags
    private boolean fullscreen=true;
    private boolean vSync=true;
    private boolean debug=false;
    private int renderingDistance=1;

    //GuiElement
    private TextInput vSyncInput;
    private TextInput debugInput;
    private TextInput fullScreenInput;
    private TextInput widthInput;
    private TextInput heightInput;
    private TextInput renderingDistanceInput;
    private Button launchGame;

    public LauncherGui(Vector2i size) {
        super(0,0,size.x,size.y);
        this.createGui(size);
    }

    private void createGui(Vector2i size) {
        Panel settings = new Panel(0, 200, size.x, 400);
        Label settingsLabel = this.createOptionLabel("Settings", settings);

        Label vSync = this.createOptionLabel("vSync", settings);
        vSyncInput = this.createOptionInput("true", settings);

        Label debugLabel = this.createOptionLabel("Debug", settings);
        debugInput = this.createOptionInput("false", settings);

        Label fullscreenLabel = this.createOptionLabel("Fullscreen", settings);
        fullScreenInput = this.createOptionInput("true", settings);


        Label widthLabel = this.createOptionLabel("Width", settings);
        widthInput = this.createOptionInput("fs", settings);
        widthInput.setEditable(false);

        Label heightLabel = this.createOptionLabel("Height", settings);
        heightInput = this.createOptionInput("fs", settings);
        heightInput.setEditable(false);


        Label widthMessage = this.createMessage("Value not correct!",
                new Vector2f(widthInput.getPosition().x + widthInput.getSize().x + 20, widthLabel.getPosition().y));
        widthInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumeric(event.getNewValue()) && !event.getNewValue().equalsIgnoreCase("fs")) {
                settings.add(widthMessage);
                Themes.getDefaultTheme().applyAll(widthMessage);
            } else {
                settings.remove(widthMessage);
            }
        });
        Label heightMessage = this.createMessage("Value not correct!",
                new Vector2f(heightInput.getPosition().x + heightInput.getSize().x + 20, heightLabel.getPosition().y));
        heightInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumeric(event.getNewValue()) && !event.getNewValue().equalsIgnoreCase("fs")) {
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

        Label renderingDistanceLabel = this.createOptionLabel("Rendering distance", settings);
        renderingDistanceInput = this.createOptionInput("1", settings);
        Label renderingDistanceMessage = this.createMessage("Value must be greater or equal to 1",
                new Vector2f(renderingDistanceInput.getPosition().x + renderingDistanceInput.getSize().x + 20, renderingDistanceLabel.getPosition().y));

        renderingDistanceInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumeric(event.getNewValue())) {
                renderingDistanceMessage.getTextState().setText("Value not numeric");
                settings.add(renderingDistanceMessage);
                Themes.getDefaultTheme().applyAll(renderingDistanceMessage);
            } else if (!this.checkRenderingDistance(event.getNewValue())) {
                renderingDistanceMessage.getTextState().setText("Value must be greater or equal to 1");
                settings.add(renderingDistanceMessage);
                Themes.getDefaultTheme().applyAll(renderingDistanceMessage);
            } else {
                settings.remove(renderingDistanceMessage);
            }
        });

        launchGame = this.createButton("Start game", new Vector2f(290, 50), new Vector2f(120, 90));
        startGame();
        add(launchGame);
        add(settings);
        switchTheme();
        Themes.getDefaultTheme().applyAll(this);
        //Set others style options AFTER theme has been applied
        settingsLabel.getStyle().setFontSize(40f);
        launchGame.getStyle().setFontSize(50f);
    }

    private void startGame() {
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction() && checkGameCanStart()) {
                try {
                    gameEngine = new GameEngine("CubicWorldSimulator",
                            vSync, new Game(), debug);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                glfwHideWindow(window);
                gameEngine.run();
            }
        });
    }

    private boolean checkGameCanStart() {
        String width=widthInput.getTextState().getText();
        String height=heightInput.getTextState().getText();
        fullscreen = parseBoolean(fullScreenInput.getTextState().getText()).orElse(fullscreen);
        vSync = parseBoolean(vSyncInput.getTextState().getText()).orElse(vSync);
        debug = parseBoolean(debugInput.getTextState().getText()).orElse(debug);
        return (isNumeric(width) ||  width.equalsIgnoreCase("fs"))  &&
                (isNumeric(height) || height.equalsIgnoreCase("fs")) &&
                this.checkRenderingDistance(renderingDistanceInput.getTextState().getText());
    }

    private boolean checkRenderingDistance (String value) {
        if (this.isNumeric(value)) {
            return (Integer.parseInt(value))>=1;
        }
        return false;
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
}