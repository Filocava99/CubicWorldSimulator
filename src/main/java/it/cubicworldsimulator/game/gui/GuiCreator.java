package it.cubicworldsimulator.game.gui;

import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.Renderer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GuiCreator{

    private static volatile boolean running = true;
    private static long[] monitors = null;
    private static boolean toggleFullscreen = false;
    private static boolean fullscreen = false;
    private static Context context;

    /**
     * GLFW commands used to help to create a new window
     */
    private static class GlfwHelper {
        /**
         * Create a new GLFW Window
         * @return long windowId
         */
        private long createWindow(String title) {
            System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
            System.setProperty("java.awt.headless", Boolean.TRUE.toString());
            if (!glfwInit()) {
                throw new RuntimeException("Can't initialize GLFW");
            }
            return glfwCreateWindow(100, 100, title, NULL, NULL);
        }

        /**
         * Destroy window Id
         * @param renderer -- destroy the render
         * @param windowId -- needed to destroy the window identified by this Id
         */
        private void destroyWindow(Renderer renderer, long windowId) {
            renderer.destroy();
            glfwDestroyWindow(windowId);
            glfwTerminate();
        }

        /**
         * Set window options: for now you can set maximized window or resizable window.
         * @param windowId -- Id of the window
         * @param maximized -- int value in GLFW library
         * @param resizable -- int value in GLFW library
         */
        private void setWindowProperty(long windowId, int maximized, int resizable) {
            if (maximized==1)
                glfwMaximizeWindow(windowId);
            glfwWindowHint(GLFW_RESIZABLE, resizable); // the window won't be resizable
        }

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
         * Set all initializer options
         * @param windowId
         * @param frame
         * @return DefaultInitializer object. It will be needed when renderer will be created
         */
        private DefaultInitializer setInitializer(long windowId, Frame frame) {
            glfwShowWindow(windowId);
            glfwMakeContextCurrent(windowId);
            GL.createCapabilities();
            glfwSwapInterval(0);
            PointerBuffer pointerBuffer = glfwGetMonitors();
            int remaining = pointerBuffer.remaining();
            DefaultInitializer initializer = new DefaultInitializer(windowId, frame);
            GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
            GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;
            initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
            initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);
            return initializer;
        }

        /**
         * Get renderer from initializer. It will be used in loop to generate window components.
         * @param initializer
         * @return org.liquidengine.legui.system.renderer.Renderer object.
         */
        private Renderer setRenderer(DefaultInitializer initializer) {
            Renderer renderer = initializer.getRenderer();
            renderer.initialize();
            context = initializer.getContext();
            return renderer;
        }
    }

    public void createGui(String title) {
        GlfwHelper glfwHelper = new GlfwHelper();
        final long windowId = glfwHelper.createWindow(title);
        glfwHelper.setWindowProperty(windowId, GL_TRUE, GL_FALSE);
        Gui gui = new LauncherGui();
        gui.setWindow(windowId);
        var myMonitor = glfwHelper.getMonitorProperty();
        Frame frame = new Frame(myMonitor.getWidth(), myMonitor.getHeight());
        createGuiElements(frame, gui);
        var initializer = glfwHelper.setInitializer(windowId, frame);
        var renderer = glfwHelper.setRenderer(initializer);
        loop(renderer, frame, windowId, initializer, myMonitor, title);
        glfwHelper.destroyWindow(renderer, windowId);
    }

    private void loop(Renderer renderer, Frame frame, long windowId, DefaultInitializer initializer,
                      MonitorProperty myMonitor, String title) {
        long time = System.currentTimeMillis();
        while (running) {
            // Also we can do it in one line
            context.updateGlfwWindow();
            Vector2i windowSize = context.getFramebufferSize();
            glClearColor(1, 1, 1, 1);
            // Set viewport size
            glViewport(0, 0, windowSize.x, windowSize.y);
            // Clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            // render frame
            renderer.render(frame, context);
            // poll events to callbacks
            glfwPollEvents();
            glfwSwapBuffers(windowId);
            //animator.runAnimations();
            // Now we need to handle events. Firstly we need to handle system events.
            // And we need to know to which frame they should be passed.
            initializer.getSystemEventProcessor().processEvents(frame, context);
            // When system events are translated to GUI events we need to handle them.
            // This event processor calls listeners added to ui components
            initializer.getGuiEventProcessor().processEvents();
            if (toggleFullscreen) {
                if (fullscreen) {
                    glfwSetWindowMonitor(windowId, NULL, 100, 100, myMonitor.getWidth(), myMonitor.getHeight(), GLFW_DONT_CARE);
                } else {
                    glfwSetWindowMonitor(windowId, monitors[0], 0, 0, myMonitor.getWidth(), myMonitor.getHeight(),
                            myMonitor.getRefreshRate());
                }
                fullscreen = !fullscreen;
                toggleFullscreen = false;
            }
            if (System.currentTimeMillis() >= time + 1000) {
                time += 1000;
                glfwSetWindowTitle(windowId, title);
            }
        }
    }

    private void createGuiElements(Frame frame, Gui gui) {
        gui.setFocusable(false);
        gui.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> gui.setSize(event.getWidth(), event.getHeight()));
        frame.getContainer().add(gui);
        frame.getContainer().setFocusable(false);
    }
}