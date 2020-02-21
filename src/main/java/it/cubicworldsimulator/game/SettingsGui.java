package it.cubicworldsimulator.game;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.theme.Themes;

import static org.liquidengine.legui.component.optional.align.HorizontalAlign.CENTER;
import static org.liquidengine.legui.component.optional.align.VerticalAlign.MIDDLE;

public class SettingsGui extends GuiType {

    public SettingsGui() {
        this(800, 600);
    }

    public SettingsGui(int width, int height) {
        super(0, 0, width, height);
        Themes.setDefaultTheme(Themes.FLAT_PETERRIVER_DARK);
        this.getStyle().setHorizontalAlign(CENTER);
        this.getStyle().setVerticalAlign(MIDDLE);
        Label settingsLabel = new Label("Settings", 30, 30, 100, 100);
        settingsLabel.getStyle().setFontSize(30f);
        settingsLabel.getStyle().setTextColor(0, 0, 0, 1);
        this.add(settingsLabel);
    }

    private Button createButton(String text, Vector2f position, Vector2f size) {
        Button button = new Button(text, position, size);
        button.getStyle().setVerticalAlign(MIDDLE);
        button.getStyle().setHorizontalAlign(CENTER);
        button.getStyle().setFontSize(20f);
        return button;
    }
}
