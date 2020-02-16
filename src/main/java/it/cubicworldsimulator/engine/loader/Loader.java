package it.cubicworldsimulator.engine.loader;

public interface Loader {
    int createVao();
    int createVbo();
    void insertDataIntoVbo(int vboId, float[] data, int index, int target, int elementDimension);
    void insertIndices(int vboId, int[] data, int indexToBind, int target);
    void cleanUp();
}
