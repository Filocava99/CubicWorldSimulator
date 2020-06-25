package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;

import java.util.Set;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;

public class MeshImpl implements Mesh {
    private final Vao myVao;
    private final Set<Vbo> vboList;
    private final Set<Vbo> textureVboList;
    private final int vertexCount;
    private final Material material;
    private final float boundingRadius;

    public MeshImpl(Material texture, float boundingRadius, int vertexCount, Vao myVao,
                    Set<Vbo> vboList, Set<Vbo> textureVboList) {
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
    public Set<Vbo> getVboList() {
        return vboList;
    }

    @Override
    public Set<Vbo> getTextureVboList() {
        return textureVboList;
    }

    @Override
    public void cleanMesh() {
        glDisableVertexAttribArray(0);
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        getVboList().forEach(myVbo -> glDeleteBuffers(myVbo.getId()));
        // Delete the texture VBO
        getTextureVboList().forEach(myVbo -> glDeleteBuffers(myVbo.getId()));
        // Delete the VAOs
        glBindVertexArray(0);
        glDeleteVertexArrays(getVao().getId());
    }
}