package it.cubicworldsimulator.engine;

public interface Loader {
    int createVao();
    int createVbo();
    void insertFloatIntoVbo(int vboId, float[] data, int index, int target, int elementDimension);
    void insertIntIntoVbo(int vboId, int[] data, int index, int target);
}
