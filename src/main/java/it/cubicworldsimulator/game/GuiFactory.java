package it.cubicworldsimulator.game;

import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.Animator;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.Renderer;
import org.liquidengine.legui.theme.Themes;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class GuiFactory {

    private static volatile boolean running = false;
    private static long[] monitors = null;
    private static boolean toggleFullscreen = false;
    private static boolean fullscreen = false;
    private static GuiType guiType;
    private static Context context;

    public static void createGui(GuiType gui, int width, int height, String title) {
        guiType=gui;
        System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
        System.setProperty("java.awt.headless", Boolean.TRUE.toString());
        if (!glfwInit()) {
            throw new RuntimeException("Can't initialize GLFW");
        }
        long window = glfwCreateWindow(width, height, "CubicWorldSimulator", NULL, NULL);
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(0);

        PointerBuffer pointerBuffer = glfwGetMonitors();
        int remaining = pointerBuffer.remaining();
        monitors = new long[remaining];
        for (int i = 0; i < remaining; i++) {
            monitors[i] = pointerBuffer.get(i);
        }


        // Firstly we need to create frame component for window.
        Frame frame = new Frame(width, height);// new Frame(WIDTH, HEIGHT);
        createGuiElements(frame, width, height);

        // We need to create legui instance one for window
        // which hold all necessary library components
        // or if you want some customizations you can do it by yourself.
        DefaultInitializer initializer = new DefaultInitializer(window, frame);

        GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
        GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;

        initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
        initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);

        // Initialization finished, so we can start render loop.
        running = true;

        // Everything can be done in one thread as well as in separated threads.
        // Here is one-thread example.

        // before render loop we need to initialize renderer
        Renderer renderer = initializer.getRenderer();
        Animator animator = AnimatorProvider.getAnimator();
        renderer.initialize();

        long time = System.currentTimeMillis();

        context = initializer.getContext();
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
            glfwSwapBuffers(window);

            animator.runAnimations();

            // Now we need to handle events. Firstly we need to handle system events.
            // And we need to know to which frame they should be passed.
            initializer.getSystemEventProcessor().processEvents(frame, context);

            // When system events are translated to GUI events we need to handle them.
            // This event processor calls listeners added to ui components
            initializer.getGuiEventProcessor().processEvents();
            if (toggleFullscreen) {
                if (fullscreen) {
                    glfwSetWindowMonitor(window, NULL, 100, 100, width, height, GLFW_DONT_CARE);
                } else {
                    GLFWVidMode glfwVidMode = glfwGetVideoMode(monitors[0]);
                    glfwSetWindowMonitor(window, monitors[0], 0, 0, glfwVidMode.width(), glfwVidMode.height(), glfwVidMode.refreshRate());
                }
                fullscreen = !fullscreen;
                toggleFullscreen = false;
            }

            if (System.currentTimeMillis() >= time + 1000) {
                time += 1000;
                glfwSetWindowTitle(window, title);
            }
        }

        // And when rendering is ended we need to destroy renderer
        renderer.destroy();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    static void createGuiElements(Frame frame, int w, int h) {
        guiType.setFocusable(false);
        guiType.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> guiType.setSize(event.getWidth(), event.getHeight()));
        frame.getContainer().add(guiType);
        frame.getContainer().setFocusable(false);
    }
}