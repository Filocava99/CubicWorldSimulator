package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.Vao;
import it.cubicworldsimulator.engine.loader.Vbo;

import java.util.List;

public class MeshImpl implements Mesh {

    private final Vao myVao;
    private final List<Vbo> vboList;
    private final List<Vbo> textureVboList;
    private final int vertexCount;
    private final Material material;
    private final float boundingRadius;

    public MeshImpl(Material texture, float boundingRadius, int vertexCount, Vao myVao,
                    List<Vbo> vboList, List<Vbo> textureVboList) {
        this.material = texture;
        this.boundingRadius = boundingRadius;
        this.vertexCount = vertexCount;
        this.myVao = myVao;
        this.vboList = vboList;
        this.textureVboList = textureVboList;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public Vao getVao() {
        return this.myVao;
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public Material getMeshMaterial() {
        return material;
    }

    @Override
    public List<Vbo> getVboList() {
        return vboList;
    }

    @Override
    public List<Vbo> getTextureVboList() {
        return textureVboList;
    }
}