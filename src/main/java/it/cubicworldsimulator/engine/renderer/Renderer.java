package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.Scene;

public interface Renderer {

    void init();

    void render(Scene scene, float width, float height);

    void clear();

}
