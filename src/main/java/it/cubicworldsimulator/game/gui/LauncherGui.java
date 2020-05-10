package it.cubicworldsimulator.game.gui;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.game.Game;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.liquidengine.legui.component.*;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;
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
    private long worldSeed;
    private String worldName;

    //GuiElement
    private TextInput vSyncInput;
    private TextInput debugInput;
    private TextInput fullScreenInput;
    private TextInput widthInput;
    private TextInput heightInput;
    private TextInput renderingDistanceInput;
    private TextInput worldSeedInput;
    private TextInput worldStringInput;
    private Button launchGame;

    //Size
    private int width;
    private int height;

    public LauncherGui() {
        super(0, 0,
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height());
        this.width= Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width();
        this.height= Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height();
        this.createGui();
    }

    private void createGui() {
        Panel settings = new Panel(0, 200, (float) this.width, this.height);
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

        Label worldSeedLabel = this.createOptionLabel("World seed", settings);
        worldSeedInput = this.createOptionInput("424243563456", settings);
        Label seedMessage = this.createMessage("Message",
                new Vector2f(worldSeedInput.getPosition().x + worldSeedInput.getSize().x + 20, worldSeedLabel.getPosition().y));
        worldSeedInput.addTextInputContentChangeEventListener(event -> {
            if (!isNumeric(event.getNewValue()) && isFiled(event.getNewValue())) {
                seedMessage.getTextState().setText("Value not numeric");
                settings.add(seedMessage);
                Themes.getDefaultTheme().applyAll(seedMessage);
            } else if (event.getNewValue().isEmpty()){
                seedMessage.getTextState().setText("Please fill this field");
                settings.add(seedMessage);
                Themes.getDefaultTheme().applyAll(seedMessage);
            } else {
                settings.remove(seedMessage);
            }
        });

        Label worldStringLabel = this.createOptionLabel("World name", settings);
        worldStringInput = this.createOptionInput("world-1", settings);
        Label nameMessage = this.createMessage("Message-1",
                new Vector2f(worldStringInput.getPosition().x + worldStringInput.getSize().x + 20, worldStringLabel.getPosition().y));
        worldStringInput.addTextInputContentChangeEventListener(event -> {
            if (event.getNewValue().isEmpty()){
                nameMessage.getTextState().setText("Please fill this field");
                settings.add(nameMessage);
                Themes.getDefaultTheme().applyAll(nameMessage);
            } else {
                settings.remove(nameMessage);
            }
        });

        launchGame = this.createButton("Start game", new Vector2f(290, 50), new Vector2f(80, 50));
        startGame();
        add(launchGame);
        add(settings);
        switchTheme();
        Themes.getDefaultTheme().applyAll(this);
        //Set others style options AFTER theme has been applied
        settingsLabel.getStyle().setFontSize(40f);
    }

    private void startGame() {
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction() && checkGameCanStart()) {
                glfwHideWindow(window);
                try {
                    gameEngine = new GameEngine("CubicWorldSimulator",
                            vSync, new Game(), debug);
                } catch (Exception e) {

                }
                gameEngine.run();
            }
        });
    }

    private boolean checkGameCanStart() {
        String width=widthInput.getTextState().getText();
        String height=heightInput.getTextState().getText();
        String worldName=worldStringInput.getTextState().getText();
        String worldSeed=worldSeedInput.getTextState().getText();
        fullscreen = parseBoolean(fullScreenInput.getTextState().getText()).orElse(fullscreen);
        vSync = parseBoolean(vSyncInput.getTextState().getText()).orElse(vSync);
        debug = parseBoolean(debugInput.getTextState().getText()).orElse(debug);
        return (isNumeric(width) ||  width.equalsIgnoreCase("fs"))  &&
                (isNumeric(height) || height.equalsIgnoreCase("fs")) &&
                this.checkRenderingDistance(renderingDistanceInput.getTextState().getText()) &&
                isFiled(worldName) && isFiled(worldSeed);
    }

    private boolean checkRenderingDistance (String value) {
        if (isNumeric(value)) {
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
                ColorConstants.white()
        ));
    }
}