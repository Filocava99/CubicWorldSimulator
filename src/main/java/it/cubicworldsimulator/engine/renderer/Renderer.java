package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.Scene;
import it.cubicworldsimulator.engine.Window;

public interface Renderer {

    void init();

    void render(Scene scene, float width, float height);

    void clear();

}
