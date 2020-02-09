package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.Scene;
import it.cubicworldsimulator.engine.Window;

public interface Renderer {

    void init(Window window) throws Exception;

    void render(Window window, Scene scene);

    void clear();

    void cleanUp();

}
