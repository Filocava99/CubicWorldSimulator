package it.cubicworldsimulator.engine;

public interface Texture {
    /**
     * It loads up texture file in memory
     * @param filename of texture
     * @throws Exception
     */
    void loadTexture(String filename) throws Exception;

    /**
     * @return id of texture
     */
    int getId();
}
