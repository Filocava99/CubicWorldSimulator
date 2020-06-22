package it.cubicworldsimulator.game.gui;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.liquidengine.legui.component.*;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Lorenzo Balzani
 */

public class Launcher extends GenericGui {
    private static final Logger logger = LogManager.getLogger(Launcher.class);
    private GameEngine gameEngine;

    //GuiElement
    private TextInput vSyncInput;
    private TextInput debugInput;
    private TextInput fullScreenInput;
    private TextInput widthInput;
    private TextInput heightInput;
    private TextInput renderingDistanceInput;
    private TextInput worldSeedInput;
    private TextInput worldStringInput;
    private TextInput daySpeedInput;
    private Button launchGame;

    //Screen size
    private final int widthScreen;
    private final int heightScreen;

    //Settings
    private final Settings.Builder mySettingsBuilder;

    private final List<Component> objects = new ArrayList<>();

    /**
     * Constructor gets resolution of primary monitor and invoke the creationGui method.
     */
    public Launcher() {
        super(0, 0,
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height(),
                new LauncherFactory());
        widthScreen = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width();
        heightScreen = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height();
        createGui();
        this.mySettingsBuilder = new Settings.Builder();
    }


    public void createGui() {
        Panel settings = new Panel(0, (float) heightScreen/5, (float) widthScreen, (float) heightScreen);
        objects.add(createOptionLabel("Settings", settings));
        objects.add(createOptionLabel("vSync", settings));
        vSyncInput = guiFactory.createTextInput("true", settings);
        objects.add(vSyncInput);
        objects.add(createOptionLabel("Debug", settings));
        debugInput = guiFactory.createTextInput("false", settings);
        objects.add(debugInput);
        objects.add(createOptionLabel("Fullscreen", settings));
        fullScreenInput = guiFactory.createTextInput("true", settings);
        objects.add(fullScreenInput);
        Label widthLabel = createOptionLabel("Width", settings);
        objects.add(widthLabel);
        widthInput = guiFactory.createTextInput("1920", settings);
        objects.add(widthInput);
        objects.add(guiFactory.createLabel("Default is 1920",
                new Vector2f(widthInput.getPosition().x + widthInput.getSize().x + 20, widthLabel.getPosition().y), settings));
        Label heightLabel = createOptionLabel("Height", settings);
        objects.add(heightLabel);
        heightInput = guiFactory.createTextInput("1080", settings);
        objects.add(heightInput);
        objects.add(guiFactory.createLabel("Default is 1080",
                new Vector2f(heightInput.getPosition().x + heightInput.getSize().x + 20, heightLabel.getPosition().y), settings));
        Label renderingDistanceLabel = createOptionLabel("Rendering distance", settings);
        objects.add(renderingDistanceLabel);
        renderingDistanceInput = guiFactory.createTextInput("1", settings);
        objects.add(renderingDistanceInput);
        objects.add(guiFactory.createLabel("Value must be greater or equal to 1",
                new Vector2f(renderingDistanceInput.getPosition().x + renderingDistanceInput.getSize().x + 20, renderingDistanceLabel.getPosition().y), settings));
        Label worldSeedLabel = createOptionLabel("World seed", settings);
        objects.add(worldSeedLabel);
        worldSeedInput = guiFactory.createTextInput("424243563456", settings);
        objects.add(worldSeedInput);
        objects.add(guiFactory.createLabel("Value must be numeric",
                new Vector2f(worldSeedInput.getPosition().x + worldSeedInput.getSize().x + 20, worldSeedLabel.getPosition().y), settings));
        Label worldStringLabel = createOptionLabel("World name", settings);
        objects.add(worldStringLabel);
        worldStringInput = guiFactory.createTextInput("world-1", settings);
        objects.add(worldStringInput);
        Label daySpeedLabel = createOptionLabel("Day speed", settings);
        objects.add(daySpeedLabel);
        daySpeedInput = guiFactory.createTextInput("100", settings);
        objects.add(daySpeedInput);
        launchGame = guiFactory.createButton("Start game", new Vector2f(290, 50), this);
        objects.add(launchGame);
        startGame();
        add(settings);
        changeTheme(MyThemes.FLAT_COLORED_THEME);
        objects.forEach(item -> item.getStyle().setFontSize(getFontSize()));
    }

    /**
     * Perform all operations with the goal of starting the game
     */
    public void startGame() {
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction() && checkGameCanStart()) {
                final Settings mySettings = performSettingsBuilder();
                glfwHideWindow(getWindowId());
                try {
                    gameEngine = new GameEngine("CubicWorldSimulator",
                                 new Game(mySettings), mySettings);
                } catch (Exception ignored) {
                }
                gameEngine.run();
            }
        });
    }

    /**
     * Creates a specific label left to text input
     * @param title of the label
     * @param panelToAdd where label will be added
     * @return created label
     */
    public Label createOptionLabel(String title, Panel panelToAdd) {
        return ((LauncherFactory)guiFactory).createOptionLabel(title, panelToAdd);
    }

    /**
     * If checks have been passed, the method below will build a Settings object by using the Builder
     */
    private Settings performSettingsBuilder() {
        return mySettingsBuilder.width(Integer.parseInt(widthInput.getTextState().getText()))
                         .height(Integer.parseInt(heightInput.getTextState().getText()))
                         .worldName(worldStringInput.getTextState().getText())
                         .worldSeed(Long.parseLong(worldSeedInput.getTextState().getText()))
                         .renderingDistance(Integer.parseInt(renderingDistanceInput.getTextState().getText()))
                         .fullscreen(Boolean.parseBoolean(fullScreenInput.getTextState().getText()))
                         .vSync(Boolean.parseBoolean(vSyncInput.getTextState().getText()))
                         .debug(Boolean.parseBoolean(debugInput.getTextState().getText()))
                         .daySpeed(Float.parseFloat(daySpeedInput.getTextState().getText()))
                         .build();
    }

    /**
     * Perform all controls to check if game can start with actual parameters.
     * @return boolean answer to all checks.
     */
    private boolean checkGameCanStart() {
        return (isNumeric(widthInput.getTextState().getText())
                && isNumeric(heightInput.getTextState().getText())
                && checkRenderingDistance(renderingDistanceInput.getTextState().getText())
                && isFiled(worldStringInput.getTextState().getText())
                && isFiled(worldSeedInput.getTextState().getText())
                && isNumeric(daySpeedInput.getTextState().getText()));
    }

    /**
     * Check if rendering distance is greater or equal to 1. If it's not the camera won't work well.
     * @param value
     * @return
     */
    private boolean checkRenderingDistance (String value) {
        if (isNumeric(value)) {
            return (Integer.parseInt(value))>=1;
        }
        return false;
    }

    public String getTitle() {
        return "CubicWorldSimulator Launcher";
    }

}