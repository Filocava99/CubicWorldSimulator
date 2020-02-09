package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.Texture;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int vertexCount;
    private final List<Integer> textureVboList = new ArrayList<>();
    private final Material material;
    private final float boundingRadius;

    public Mesh(float[] positions, float[] textCoords, int[] indices, Material texture, float boundingRadius) {
        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        this.material = texture;
        this.boundingRadius = boundingRadius;

        try {
            vertexCount = indices.length;
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            /* Colour VBO
            colourVboId = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);*/

            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            //Texture VBO
            int vboId = glGenBuffers();
            textureVboList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
        }
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