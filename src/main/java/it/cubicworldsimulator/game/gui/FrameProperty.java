package it.cubicworldsimulator.game.gui;

import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor;
import org.liquidengine.legui.system.renderer.Renderer;

/**
 * @author Lorenzo Balzani
 */

public class FrameProperty {
    private long windowId;
    private Renderer renderer;
    private Frame frame;
    private Context context;
    private SystemEventProcessor systemEventProcessor;
    private GenericGui gui;

    public GenericGui getGui() {
        return gui;
    }

    public void setGui(GenericGui gui) {
        this.gui = gui;
    }

    public SystemEventProcessor getSystemEventProcessor() {
        return systemEventProcessor;
    }

    public void setSystemEventProcessor(SystemEventProcessor systemEventProcessor) {
        this.systemEventProcessor = systemEventProcessor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getWindowId() {
        return windowId;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void setWindowId(long windowId) {
        this.windowId = windowId;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
