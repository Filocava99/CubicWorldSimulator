package it.cubicworldsimulator.game.gui;

import org.joml.Vector2i;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.system.context.CallbackKeeper;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.context.DefaultCallbackKeeper;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessorImpl;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GuiContainer {

    private static volatile boolean running = false;
    private List<FrameProperty> myFrames = new ArrayList<>();
    private final GlfwHelper glfwHelper = new GlfwHelper();

    private class GlfwHelper {
        /**
         * @return resolution and refresh rate of primary monitor
         */
        private MonitorProperty getMonitorProperty() {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null;
            var myMonitor = new MonitorProperty();
            myMonitor.setHeight(vidmode.height());
            myMonitor.setWidth(vidmode.width());
            myMonitor.setRefreshRate(vidmode.refreshRate());
            return myMonitor;
        }

        /**
         * Destroy window Id
         */
        private void destroyWindows() {
            for (FrameProperty myFrame : myFrames) {
                myFrame.getRenderer().destroy();
                glfwDestroyWindow(myFrame.getWindowId());
            }
            glfwTerminate();
        }

        /**
         * Creates a new window and sets the context
         * @param title of the window
         * @param myMonitor encapsulate information about the monitor
         * @return
         */
        private long createWindow(String title, MonitorProperty myMonitor) {
            long windowId = glfwCreateWindow(myMonitor.getWidth(), myMonitor.getHeight(),
                    title, NULL, NULL);
            glfwShowWindow(windowId);
            glfwMakeContextCurrent(windowId);
            GL.createCapabilities();
            glfwSwapInterval(0);
            return windowId;
        }

        /**
         * Creates a keeper for input callbacks
         * @param windowId
         * @return
         */
        private CallbackKeeper createKeeper(long windowId) {
            CallbackKeeper keeper = new DefaultCallbackKeeper();
            CallbackKeeper.registerCallbacks(windowId, keeper);
            return keeper;
        }
    }

    public GuiContainer() {
        glfwInit();
        System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
        System.setProperty("java.awt.headless", Boolean.TRUE.toString());
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Can't initialize GLFW");
        }
    }

    public void init(List<GenericGui> guiList) {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
        GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;
        MonitorProperty myMonitor = glfwHelper.getMonitorProperty();
        for (int i = 0; i < guiList.size(); i++) {
            FrameProperty myFrameProperty = new FrameProperty();
            myFrames.add(myFrameProperty);
            long windowId = glfwHelper.createWindow(guiList.get(i).getTitle(), myMonitor);
            guiList.get(i).setWindowId(windowId);
            myFrameProperty.setWindowId(windowId);
            myFrameProperty.setRenderer(new NvgRenderer());
            myFrameProperty.getRenderer().initialize();
            myFrames.get(i).setFrame(new Frame(myMonitor.getWidth(), myMonitor.getHeight()));
            createGuiElements(myFrames.get(i).getFrame(), guiList.get(i));
            myFrameProperty.setContext(new Context(windowId));
            CallbackKeeper keeper = glfwHelper.createKeeper(windowId);
            keeper.getChainKeyCallback().add(glfwKeyCallbackI);
            keeper.getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);
            myFrameProperty.setSystemEventProcessor(new SystemEventProcessorImpl());
            SystemEventProcessor.addDefaultCallbacks(keeper, myFrameProperty.getSystemEventProcessor());
        }
        loop();
        glfwHelper.destroyWindows();
    }

    private void loop() {
        running = true;
        while (running) {
            for (FrameProperty myFrame : myFrames) {
                glfwMakeContextCurrent(myFrame.getWindowId());
                GL.getCapabilities();
                glfwSwapInterval(0);
                myFrame.getContext().updateGlfwWindow();
                Vector2i windowSize = myFrame.getContext().getFramebufferSize();
                glClearColor(1, 1, 1, 1);
                glViewport(0, 0, windowSize.x, windowSize.y);
                glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
                myFrame.getRenderer().render(myFrame.getFrame(), myFrame.getContext());
                glfwPollEvents();
                glfwSwapBuffers(myFrame.getWindowId());
                myFrame.getSystemEventProcessor().processEvents(myFrame.getFrame(), myFrame.getContext());
                EventProcessorProvider.getInstance().processEvents();
                LayoutManager.getInstance().layout(myFrame.getFrame());
            }
        }
    }

    private void createGuiElements(Frame frame, GenericGui myGui) {
        myGui.setFocusable(false);
        myGui.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> {
            myGui.setSize(event.getWidth(), event.getHeight());
        });
        frame.getContainer().add(myGui);
        frame.getContainer().setFocusable(false);
    }
}