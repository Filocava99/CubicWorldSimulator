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
    private final Material material;
    private final float boundingRadius;

    public Mesh (float[] positions, float[] textCoords, int[] indices, String textureFileName) throws Exception {
        this(positions, textCoords, indices, new Material(new TextureLoaderImpl().loadTexture(textureFileName)), 0);
    }

    public Mesh(float[] positions, float[] textCoords, int[] indices, Material texture, float boundingRadius) {
        this.loader = new LoaderImpl();
        this.material = texture;
        this.boundingRadius = boundingRadius;

        this.vaoId = this.loader.createVao();
        vertexCount = indices.length;

        // Position VBO
        this.posVboId = this.loader.createVbo();
        this.loader.insertFloatIntoVbo(posVboId, positions, 0, GL_ARRAY_BUFFER, 3);

        // Index VBO
        this.idxVboId = this.loader.createVbo();
        this.loader.insertIntIntoVbo(idxVboId, indices, 0, GL_ELEMENT_ARRAY_BUFFER);

        //Texture VBO
        int textureVboId = this.loader.createVbo();
        this.textureVboList.add(textureVboId);
        this.loader.insertFloatIntoVbo(textureVboId, textCoords, 1, GL_ARRAY_BUFFER, 2);

        //Cleanup
        this.cleanUp();
    }

    public void render() {
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
        // Draw the mesh
        glBindVertexArray(getVaoId());
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        // Restore state
        glBindVertexArray(0);
    }

    private void initRender() {
        Texture texture = material.getTexture();
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());
    }

    private void endRender() {
        // Restore state
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer) {
        initRender();

        for (GameItem gameItem : gameItems) {
            // Set up data required by gameItem
            consumer.accept(gameItem);
            // Render this game item
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        endRender();
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

    public Material getMaterial() {
        return material;
    }
}