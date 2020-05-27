package it.cubicworldsimulator.engine.loader;

import it.cubicworldsimulator.engine.graphic.Texture;

public interface TextureLoader {
    /**
     * It loads up texture file in memory
     * @param filename of texture
     */
    Texture loadTexture(String filename);
}
