package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.Loader;
import it.cubicworldsimulator.engine.loader.LoaderImpl;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;


public class Mesh {
    private final static Logger logger = LogManager.getLogger(Mesh.class);
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
        logger.debug("Mesh clean up");
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