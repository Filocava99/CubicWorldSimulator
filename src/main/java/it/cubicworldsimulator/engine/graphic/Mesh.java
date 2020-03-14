package it.cubicworldsimulator.engine.graphic;

import java.util.List;

public class Mesh {

    private int vaoId;
    private final List<Integer> vboList;
    private final List<Integer> textureVboList;
    private final int vertexCount;
    private final MeshMaterial material;
    private final float boundingRadius;

    public Mesh(MeshMaterial texture, float boundingRadius, int vertexCount, int vaoId,
                List<Integer> vboList, List<Integer> textureVboList) {
        this.material = texture;
        this.boundingRadius = boundingRadius;
        this.vertexCount = vertexCount;
        this.vaoId = vaoId;
        this.vboList = vboList;
        this.textureVboList = textureVboList;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoId() {
        return this.vaoId;
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public MeshMaterial getMeshMaterial() {
        return material;
    }
}