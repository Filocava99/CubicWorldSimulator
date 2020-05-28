package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.Vao;
import it.cubicworldsimulator.engine.loader.Vbo;

import java.util.List;

public class Mesh {

    private final Vao myVao;
    private final List<Vbo> vboList;
    private final List<Vbo> textureVboList;
    private final int vertexCount;
    private final Material material;
    private final float boundingRadius;

    public Mesh(Material texture, float boundingRadius, int vertexCount, Vao myVao,
                List<Vbo> vboList, List<Vbo> textureVboList) {
        this.material = texture;
        this.boundingRadius = boundingRadius;
        this.vertexCount = vertexCount;
        this.myVao = myVao;
        this.vboList = vboList;
        this.textureVboList = textureVboList;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Vao getVao() {
        return this.myVao;
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public Material getMeshMaterial() {
        return material;
    }

    public List<Vbo> getVboList() {
        return vboList;
    }

    public List<Vbo> getTextureVboList() {
        return textureVboList;
    }
}