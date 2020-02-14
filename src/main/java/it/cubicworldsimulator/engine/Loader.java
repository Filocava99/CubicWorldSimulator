package it.cubicworldsimulator.engine;

public interface Loader {
    int createVao();
    int createVbo();
    void insertFloatIntoVbo(float[] data, int index);
    void insertIntIntoVbo(int[] data, int index);
    void activateVbo(int index);
}
