package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.MouseInput;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWWindowCloseCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.GL_FALSE;

public class GameEngine extends Thread {

    public static final int TARGET_FPS = 75; //frames per second

    public static final int TARGET_UPS = 30; //updates per second

    private final Window window;

    private  final MouseInput mouseInput;

    private final Timer timer;

    private final GameLogic gameLogic;

    public GameEngine(String windowTitle, boolean vSync, GameLogic gameLogic, boolean debug) throws Exception {
        this(windowTitle, 0, 0, vSync, gameLogic, debug);
    }

    public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogic gameLogic, boolean debug) throws Exception {
        Vector4f clearColor = new Vector4f(0.0f,0.0f,255.0f,0.0f); //TODO Creare diversi costruttori in modo da passare il clearColor facoltativamente
        window = new Window(windowTitle, width, height, clearColor, vSync, debug);
        mouseInput = new MouseInput(window);
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }finally {
            cleanUp();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init();
        gameLogic.init(window);
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }finally {
                cleanUp();
            }
        }
    }

    protected void input() {
        mouseInput.input();
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }

    protected void cleanUp(){
        gameLogic.cleanUp();
        glfwHideWindow(window.getWindowHandle());
        System.exit(0);
    }
}