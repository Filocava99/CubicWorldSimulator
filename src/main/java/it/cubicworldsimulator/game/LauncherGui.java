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

    private long window;

    private boolean fullscreen=true;
    private boolean vSync=true;
    private boolean debug=false;

    public LauncherGui(Vector2i size) {
        super(0,0,size.x,size.y);
        Panel settings = new Panel(150, 200, 400, 300);
        Label settingsLabel = new Label("Settings");
        settingsLabel.getStyle().setFontSize(50f);
        settingsLabel.setPosition(settingsLabel.getPosition().x+5, settingsLabel.getPosition().y+30);
        settings.add(settingsLabel);

        Label fullScreenLabel = new Label("Fullscreen");
        fullScreenLabel.getStyle().setFontSize(30f);
        settings.add(fullScreenLabel);
        fullScreenLabel.setPosition(fullScreenLabel.getPosition().x+5, fullScreenLabel.getPosition().y+80);

        TextInput fullScreenInput = new TextInput(fullScreenLabel.getPosition().x+150, fullScreenLabel.getPosition().y, 70, 35);
        fullScreenInput.getTextState().setText("true");
        fullScreenInput.getStyle().setFontSize(20f);
        fullScreenInput.getStyle().setTextColor(0,0,0,1);
        fullScreenInput.getStyle().setHorizontalAlign(CENTER);
        fullScreenInput.getStyle().setVerticalAlign(MIDDLE);
        fullScreenInput.getStyle().getBackground().setColor(ColorConstants.white());
        settings.add(fullScreenInput);
        fullScreenInput.getStyle().setFontSize(30f);

        Label vSyncLabel = new Label("vSync");
        vSyncLabel.getStyle().setFontSize(30f);
        vSyncLabel.setPosition(vSyncLabel.getPosition().x+5, vSyncLabel.getPosition().y+130);
        settings.add(vSyncLabel);

        TextInput vSyncInput = new TextInput(vSyncLabel.getPosition().x+150, vSyncLabel.getPosition().y, 70, 35);
        vSyncInput.getTextState().setText("true");
        vSyncInput.getStyle().setFontSize(20f);
        vSyncInput.getStyle().setTextColor(0,0,0,1);
        vSyncInput.getStyle().setHorizontalAlign(CENTER);
        vSyncInput.getStyle().setVerticalAlign(MIDDLE);
        vSyncInput.getStyle().getBackground().setColor(ColorConstants.white());
        settings.add(vSyncInput);
        vSyncInput.getStyle().setFontSize(30f);

        Label debugLabel = new Label("Debug");
        debugLabel.getStyle().setFontSize(30f);
        debugLabel.setPosition(debugLabel.getPosition().x+5, debugLabel.getPosition().y+180);
        settings.add(debugLabel);

        TextInput debugInput = new TextInput(debugLabel.getPosition().x+150, debugLabel.getPosition().y, 70, 35);
        debugInput.getTextState().setText("false");
        debugInput.getStyle().setFontSize(20f);
        debugInput.getStyle().setTextColor(0,0,0,1);
        debugInput.getStyle().setHorizontalAlign(CENTER);
        debugInput.getStyle().setVerticalAlign(MIDDLE);
        debugInput.getStyle().getBackground().setColor(ColorConstants.white());
        settings.add(debugInput);
        debugInput.getStyle().setFontSize(30f);

        Button launchGame = this.createButton("Start game", new Vector2f(100, 50), new Vector2f(120, 90));
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction()) {
                System.out.println(debugInput.getTextState().getText());
                if(fullScreenInput.getTextState().getText().equalsIgnoreCase("false") || (fullScreenInput.getTextState().getText().equalsIgnoreCase("true"))) {
                    this.fullscreen = Boolean.parseBoolean(fullScreenInput.getTextState().getText());
                }
                if(vSyncInput.getTextState().getText().equalsIgnoreCase("false") || (vSyncInput.getTextState().getText().equalsIgnoreCase("true"))) {
                    this.vSync = Boolean.parseBoolean(vSyncInput.getTextState().getText());
                }
                if(debugInput.getTextState().getText().equalsIgnoreCase("false") || (debugInput.getTextState().getText().equalsIgnoreCase("true"))) {
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
        launchGame.setPosition(290, 100);
        this.add(launchGame);
        this.add(settings);
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
        Themes.getDefaultTheme().applyAll(this);
        settingsLabel.getStyle().setFontSize(40f);
    }

    private Button createButton(String text, Vector2f position, Vector2f size) {
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(30f);
        button.getStyle().setHorizontalAlign(CENTER);
        button.getStyle().setVerticalAlign(MIDDLE);
        button.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
        return button;
    }

    @Override
    public void setWindow (long window) {
        this.window=window;
    }
}