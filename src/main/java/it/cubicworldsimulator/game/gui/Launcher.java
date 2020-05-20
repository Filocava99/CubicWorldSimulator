package it.cubicworldsimulator.game.gui;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static org.liquidengine.legui.style.color.ColorUtil.fromInt;
import static org.lwjgl.glfw.GLFW.*;

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
    private Button launchGame;

    //Screen size
    private final int widthScreen;
    private final int heightScreen;

    //Settings
    private final Settings.Builder mySettingsBuilder;

    /**
     * Constructor gets resolution of primary monitor and invoke the creationGui method.
     */
    public Launcher() {
        super(0, 0,
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height());
        widthScreen = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width();
        heightScreen = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height();
        createGui();
        this.mySettingsBuilder = new Settings.Builder();
    }

    /**
     * It creates gui components
     */
    private void createGui() {
        Panel settings = new Panel(0, 200, (float) widthScreen, heightScreen);
        Label settingsLabel = createOptionLabel("Settings", settings);
        createOptionLabel("vSync", settings);
        vSyncInput = createTextInput("true", settings);
        createOptionLabel("Debug", settings);
        debugInput = createTextInput("false", settings);
        createOptionLabel("Fullscreen", settings);
        fullScreenInput = createTextInput("true", settings);
        Label widthLabel = createOptionLabel("Width", settings);
        widthInput = createTextInput("1920", settings);
        createLabel("Default is 1920",
                new Vector2f(widthInput.getPosition().x + widthInput.getSize().x + 20, widthLabel.getPosition().y), settings);
        Label heightLabel = createOptionLabel("Height", settings);
        heightInput = createTextInput("1080", settings);
        createLabel("Default is 1080",
                new Vector2f(heightInput.getPosition().x + heightInput.getSize().x + 20, heightLabel.getPosition().y), settings);
        Label renderingDistanceLabel = createOptionLabel("Rendering distance", settings);
        renderingDistanceInput = createTextInput("1", settings);
        createLabel("Value must be greater or equal to 1",
                new Vector2f(renderingDistanceInput.getPosition().x + renderingDistanceInput.getSize().x + 20, renderingDistanceLabel.getPosition().y), settings);
        Label worldSeedLabel = createOptionLabel("World seed", settings);
        worldSeedInput = createTextInput("424243563456", settings);
        createLabel("Value must be numeric",
                new Vector2f(worldSeedInput.getPosition().x + worldSeedInput.getSize().x + 20, worldSeedLabel.getPosition().y), settings);
        Label worldStringLabel = createOptionLabel("World name", settings);
        worldStringInput = createTextInput("world-1", settings);
        createLabel("World name can be whatever you want",
                new Vector2f(worldStringInput.getPosition().x + worldStringInput.getSize().x + 20, worldStringLabel.getPosition().y), settings);
        launchGame = createButton("Start game", new Vector2f(290, 50), new Vector2f(80, 50));
        startGame();
        add(launchGame);
        add(settings);
        switchTheme();
        Themes.getDefaultTheme().applyAll(this);
        //Set other style options AFTER theme has been applied
        settingsLabel.getStyle().setFontSize(40f);
    }

    /**
     * Perform all operations with the goal of starting the game
     */
    private void startGame() {
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction() && checkGameCanStart()) {
                final Settings mySettings = performSettingsBuilder();
                glfwHideWindow(windowId);
                try {
                    gameEngine = new GameEngine("CubicWorldSimulator",
                                 new Game(mySettings), mySettings);
                } catch (Exception e) {
                }
                gameEngine.run();
            }
        });
    }

    /**
     * Creates a specific label left to text input
     * @param title of the label
     * @param panelToAdd where label will be added
     * @return
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
                         .fullscreen(Boolean.parseBoolean(fullScreenInput.getTextState().getText()))
                         .vSync(Boolean.parseBoolean(vSyncInput.getTextState().getText()))
                         .debug(Boolean.parseBoolean(debugInput.getTextState().getText()))
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
                && isFiled(worldSeedInput.getTextState().getText()));
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

    /**
     * Switch from any theme to the default one.
     */
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

    public String getTitle() {
        return "CubicWorldSimulator Launcher";
    }
}