package it.cubicworldsimulator.engine.Hud;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import it.cubicworldsimulator.engine.Window;
import it.cubicworldsimulator.engine.loader.LoaderUtility;
import it.cubicworldsimulator.game.utility.Pair;
import org.lwjgl.nanovg.NVGColor;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UpperHud implements GenericHud {

    private static final String FONT_NAME = "BOLD";

    private long vg;

    private ByteBuffer fontBuffer;

    private DoubleBuffer posx;

    private DoubleBuffer posy;

    private String hour;



    public void init() throws Exception {
        vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (vg == NULL) {
            throw new Exception("Could not init nanovg");
        }
        fontBuffer = LoaderUtility.ioResourceToByteBuffer("/fonts/OpenSans-Bold.ttf", 150 * 1024);
        int font = nvgCreateFontMem(vg, FONT_NAME, fontBuffer, 0);
        if (font == -1) {
            throw new Exception("Could not add font");
        }
        posx = MemoryUtil.memAllocDouble(1);
        posy = MemoryUtil.memAllocDouble(1);
    }

    @Override
    public void renderHud(Window window) {
        nvgBeginFrame(vg, window.getWidth(), window.getHeight(), 1);
        //Upper bar
        nvgBeginPath(vg);
        nvgRect(vg, 0,0, window.getWidth(), 50);
        nvgFillColor(vg, fromRgbToNvg(0x0, 0x0, 0x66, 100));
        nvgFill(vg);
        glfwGetCursorPos(window.getWindowId(), posx, posy);
        createText("Hour: " + getHour(), FONT_NAME, 40f,
                new Pair<>(0, 0),fromRgbToNvg(0xe6, 0xea, 0xed, 255));
        createText("Move: WASD - Up Space - Down: Shift - Third/first person: T/P", FONT_NAME, 30f,
                new Pair<>(window.getWidth()/2 + window.getWidth()/8, 0), fromRgbToNvg(0xe6, 0xea, 0xed, 255));
        nvgEndFrame(vg);
        // Restore state
        window.restoreState();
    }

    @Override
    public void createText(String text, String fontName, float fontSize, Pair<Integer, Integer> position,
                           NVGColor color) {
        nvgFontSize(vg, fontSize);
        nvgFontFace(vg, fontName);
        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgFillColor(vg, color);
        nvgText(vg, position.getFirstValue(), position.getSecondValue(), text);
    }


    public void cleanup() {
        nvgDelete(vg);
        if (posx != null) {
            MemoryUtil.memFree(posx);
        }
        if (posy != null) {
            MemoryUtil.memFree(posy);
        }
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    private String getHour() {
        return hour;
    }
}