package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;

import static org.liquidengine.legui.style.color.ColorUtil.fromInt;

public abstract class GenericGui extends Panel {
    protected final GuiFactory guiFactory;
    private long windowId;

    protected GenericGui(int x, int y, float width, float height) {
        super(x, y, width, height);
        guiFactory = new LauncherFactory();
        guiFactory.setWidth((int) width);
        guiFactory.setHeight((int) height);
        this.setAspectRatio();
    }

    /**
     * It creates gui components
     */
    protected abstract void createGui();

    public void setAspectRatio() {
        guiFactory.setAspectRatio();
    }

    public Button createButton(String text, Vector2f position, Vector2f size) {
        return guiFactory.createButton(text, position, size);
    }

    public Label createLabel(String text, Vector2f position, Panel panelToAdd) {
        return guiFactory.createLabel(text, position, panelToAdd);
    }

    public TextInput createTextInput(String title, Panel panelToAdd) {
        return guiFactory.createTextInput(title, panelToAdd);
    }

    protected boolean isFiled(String text) {
        return !text.isEmpty();
    }

    public abstract String getTitle();

    protected long getWindowId() {
        return windowId;
    }

    protected void setWindowId(long windowId) {
        this.windowId = windowId;
    }

    /**
     * Switch to the different theme
     */
    protected abstract void changeTheme();

    protected void flatColoredTheme() {
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
       applyTheme();
    }

    private void applyTheme() {
        Themes.getDefaultTheme().applyAll(this);
    }
}
