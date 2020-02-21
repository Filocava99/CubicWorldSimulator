package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.flex.FlexStyle;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;

import static org.liquidengine.legui.component.optional.align.HorizontalAlign.CENTER;
import static org.liquidengine.legui.component.optional.align.VerticalAlign.MIDDLE;
import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;

public class LauncherGui extends GuiType {

    public LauncherGui() {
        this(800, 600);
    }

    public LauncherGui(int width, int height) {
        super(0, 0, width, height);
        Panel panel = new Panel(0, 0, width, height);

        Button launchGame = this.createButton("Start game", new Vector2f(100, 50), new Vector2f(120, 90));
        launchGame.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction()) {
                try {
                    GameEngine gameEngine = new GameEngine("CubicWorldSimulator",
                            true, new Game(), false);
                    gameEngine.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button settings = this.createButton("Settings", new Vector2f(100, 200), new Vector2f(120, 90));
        settings.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (CLICK == event.getAction()) {
                GuiFactory.createGui(new SettingsGui(), 800, 600, "CubicWorldSimulator Settings");
            }
        });
        this.add(panel);
        panel.add(launchGame);
        panel.add(settings);

    }

    private Button createButton(String text, Vector2f position, Vector2f size) {
        Button button = new Button(text, position, size);
        button.getStyle().setFontSize(30f);
        button.getStyle().setHorizontalAlign(CENTER);
        button.getStyle().setVerticalAlign(MIDDLE);
        button.getStyle().setFont(FontRegistry.ROBOTO_BOLD);
        return button;
    }
}