package it.cubicworldsimulator.engine.loader;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.graphic.Mesh;

/**
 * It creates a Mesh by using builder pattern. Invoke wanted methods before build().
 */
public interface MeshBuilder {
    MeshBuilder addPositions(float[] positions);

    MeshBuilder addIndices(int[] indices);

    MeshBuilder addTextCoords(float[] textCoords);

    MeshBuilder addNormals(float[] normals);

    MeshBuilder addTexture(Material texture);

    MeshBuilder setBoundingRadius(float value);

    Mesh build();
}
