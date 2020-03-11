package it.cubicworldsimulator.game.gui;

import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.Animator;
import org.liquidengine.legui.animation.AnimatorProvider;
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


public class GuiFactory{

    private volatile boolean running = true;
    private static long[] monitors = null;
    private static boolean toggleFullscreen = false;
    private static boolean fullscreen = false;
    private Gui gui;
    private static Context context;
    private int width;
    private int height;
    private int refreshRate;

    public void createGui(String title) {
        System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
        System.setProperty("java.awt.headless", Boolean.TRUE.toString());
        if (!glfwInit()) {
            throw new RuntimeException("Can't initialize GLFW");
        }

        long window = glfwCreateWindow(100, 100, "CubicWorldSimulator", NULL, NULL);
        glfwMaximizeWindow(window);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window won't be resizable
        this.gui = new LauncherGui();
        gui.setWindow(window);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        glfwSwapInterval(0);
        PointerBuffer pointerBuffer = glfwGetMonitors();
        int remaining = pointerBuffer.remaining();


        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null;
        this.width=vidmode.width();
        this.height=vidmode.height();
        this.refreshRate=vidmode.refreshRate();

        // Firstly we need to create frame component for window.
        Frame frame = new Frame(this.width, this.height);// new Frame(WIDTH, HEIGHT);
        createGuiElements(frame);
        // We need to create legui instance one for window
        // which hold all necessary library components
        // or if you want some customizations you can do it by yourself.
        DefaultInitializer initializer = new DefaultInitializer(window, frame);

        GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
        GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;

        initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
        initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);

        // Initialization finished, so we can start render loop.

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
                    glfwSetWindowMonitor(window, monitors[0], 0, 0, this.width, this.height, this.refreshRate);
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

    void createGuiElements(Frame frame) {
        this.gui.setFocusable(false);
        gui.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> gui.setSize(event.getWidth(), event.getHeight()));
        frame.getContainer().add(gui);
        frame.getContainer().setFocusable(false);
    }
}