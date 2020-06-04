package it.cubicworldsimulator.engine.Hud;

import it.cubicworldsimulator.engine.Window;
import it.cubicworldsimulator.game.utility.Pair;
import org.lwjgl.nanovg.NVGColor;

public interface GenericHud {
    /**
     * Init all needed libraries
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Render on screen all items
     * @param window
     */
    void renderHud(Window window);

    /**
     * Create a text on screen
     * @param text of the message
     * @param fontName --
     * @param fontSize --
     * @param position --
     * @param color of class NVGColor. You can obtain an instance of that class by using
     *              the default implementation of the method fromRgbToNvg or
     *              you can override it.
     */
    void createText(String text, String fontName, float fontSize, Pair<Integer, Integer> position,
                    NVGColor color);

    /**
     * It deletes all buffers.
     */
    void cleanup();

    /**
     * Utility class used for converting RGBA color into NVGColor.
     * @param r --
     * @param g --
     * @param b --
     * @param a --
     * @return NVGColor instance.
     */
    default NVGColor fromRgbToNvg(int r, int g, int b, int a) {
        NVGColor colour = NVGColor.create();
        colour.r(r/255.0f);
        colour.g(g/255.0f);
        colour.b(b/255.0f);
        colour.a(a/255.0f);
        return colour;
    }
}
