package it.cubicworldsimulator.engine.graphic;

public interface TextureLoader {
    /**
     * It loads up texture file in memory
     * @param filename of texture
     */
    Texture loadTexture(String filename) throws Exception;
}
