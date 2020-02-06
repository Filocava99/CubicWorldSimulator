package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameLogic;
import it.cubicworldsimulator.engine.Renderer;
import it.cubicworldsimulator.engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Game implements GameLogic {
    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    public Game() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, null);
    }

    @Override
    public void cleanUp() {

    }
}
