package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.VAO;
import it.cubicworldsimulator.engine.loader.VBO;

import java.util.List;

public class Mesh {

    private final VAO myVao;
    private final List<VBO> vboList;
    private final List<VBO> textureVboList;
    private final int vertexCount;
    private final Material material;
    private final float boundingRadius;

    public Mesh(Material texture, float boundingRadius, int vertexCount, VAO myVao,
                List<VBO> vboList, List<VBO> textureVboList) {
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

    public VAO getVao() {
        return this.myVao;
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public Material getMeshMaterial() {
        return material;
    }

    public List<VBO> getVboList() {
        return vboList;
    }

    public List<VBO> getTextureVboList() {
        return textureVboList;
    }
}