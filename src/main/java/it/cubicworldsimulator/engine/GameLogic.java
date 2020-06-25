package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.MouseInput;

public interface GameLogic {

    /**
     * Initializes the game
     * @param window main window instance
     */
    void init(Window window) throws Exception;

    /**
     * Handles the input
     * @param window main window instance
     * @param mouseInput mouse handler
     */
    void input(Window window, MouseInput mouseInput);

    /**
     * Updates the game status (camera position, world, etc)
     * @param interval interval for the update
     * @param mouseInput mouse handler
     */
    void update(float interval, MouseInput mouseInput);

    /**
     * Renders the game scene
     * @param window main window instance
     */
    void render(Window window);

    /**
     * Frees the memory of the GPU
     */
    void cleanUp();
}
