package it.cubicworldsimulator.game.gui;

import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;

import static org.liquidengine.legui.style.color.ColorUtil.fromInt;

/**
 * It models a generic LEGUI gui
 * @author Lorenzo Balzani
 */

public abstract class GenericGui extends Panel {
    protected final GuiFactory guiFactory;
    private long windowId;

    protected GenericGui(int x, int y, float width, float height, GuiFactory guiFactory) {
        super(x, y, width, height);
        this.guiFactory = guiFactory;
        guiFactory.setWidth((int) width);
        guiFactory.setHeight((int) height);
        this.setAspectRatio();
    }

    /**
     * It creates the GUI items.
     */
    protected abstract void createGui();

    /**
     * Set the aspect ration for the gui
     */
    public void setAspectRatio() {
        guiFactory.setAspectRatio();
    }

    /**
     * @param text
     * @return text is filed
     */
    protected boolean isFiled(String text) {
        return !text.isEmpty();
    }

    /**
     * @return the window title
     */
    public abstract String getTitle();

    /**
     * @return the window id in which the gui is
     */
    protected long getWindowId() {
        return windowId;
    }

    /**
     * Set the window id
     * @param windowId
     */
    protected void setWindowId(long windowId) {
        this.windowId = windowId;
    }

    /**
     * @return the font size
     */
    protected float getFontSize() {
        return guiFactory.getFontSize();
    }

    /**
     * Switch to the different theme
     */
    protected void changeTheme(MyThemes theme) {
        switch (theme) {
            case FLAT_COLORED_THEME:
                Themes.setDefaultTheme(new FlatColoredTheme(
                        fromInt(44, 62, 80, 1), // backgroundColor
                        fromInt(127, 140, 141, 1), // borderColor
                        fromInt(127, 140, 141, 1), // sliderColor
                        fromInt(2, 119, 189, 1), // strokeColor
                        fromInt(39, 174, 96, 1), // allowColor
                        fromInt(192, 57, 43, 1), // denyColor
                        fromInt(0, 0, 0, 1f),  // shadowColor
                        ColorConstants.white(),
                        FontRegistry.getDefaultFont(), 30f
                ));
                break;
            default:
                throw new IllegalArgumentException();
        }
        Themes.getDefaultTheme().applyAll(this);
    }
}
