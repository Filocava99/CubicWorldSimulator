package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.MouseInput;

public interface GameLogic {
    void init(Window window) throws Exception;

    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render(Window window);

    void cleanUp();
}
