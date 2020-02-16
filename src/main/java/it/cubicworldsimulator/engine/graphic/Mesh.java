package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.Loader;
import it.cubicworldsimulator.engine.LoaderImpl;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;


public class Mesh {
    private final Loader loader;
    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int vertexCount;
    private final List<Integer> textureVboList = new ArrayList<>();
    private final MeshMaterial material;
    private final float boundingRadius;

    public Mesh (float[] positions, float[] textCoords, int[] indices, String textureFileName) throws Exception {
        this(positions, textCoords, indices, new MeshMaterial(new TextureLoaderImpl().loadTexture(textureFileName)), 0);
    }

    public Mesh(float[] positions, float[] textCoords, int[] indices, MeshMaterial texture, float boundingRadius) {
        this.loader = new LoaderImpl();
        this.material = texture;
        this.boundingRadius = boundingRadius;

        this.vaoId = this.loader.createVao();
        vertexCount = indices.length;

        // Position VBO
        this.posVboId = this.loader.createVbo();
        this.loader.insertDataIntoVbo(posVboId, positions, 0, GL_ARRAY_BUFFER, 3);

        // Index VBO
        this.idxVboId = this.loader.createVbo();
        this.loader.insertIndices(idxVboId, indices, 0, GL_ELEMENT_ARRAY_BUFFER);

        //Texture VBO
        int textureVboId = this.loader.createVbo();
        this.textureVboList.add(textureVboId);
        this.loader.insertDataIntoVbo(textureVboId, textCoords, 1, GL_ARRAY_BUFFER, 2);

        //Cleanup
        this.loader.cleanUp();
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);
        this.textureVboList.forEach(GL15::glDeleteBuffers);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public MeshMaterial getMeshMaterial() {
        return material;
    }
}