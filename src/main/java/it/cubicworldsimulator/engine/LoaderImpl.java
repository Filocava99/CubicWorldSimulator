package it.cubicworldsimulator.engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LoaderImpl implements Loader {

    private int vaoId;
    private int vboId;

    private final float[] triangleDemo = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    private int createVAO() {
        /* VAO created and vaoID stored */
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        return vaoId;
    }

    private int createVBO() {
        /* VBO created and vboID stored */
        int vboId = glGenBuffers();
        /* Bind VBO with VAO */
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        return vboId;
    }

    private void insertIntoVBO(FloatBuffer verticesBuffer, int indexToPut) {
        /* Insert vertices in VBO */
        glBufferData(GL_ARRAY_BUFFER,
                verticesBuffer,
                GL_STATIC_DRAW);
        /* Mark attribute index 0 with vertex */
        glEnableVertexAttribArray(indexToPut);
        /* Mark data */
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    private void cleanUp(FloatBuffer verticesBuffer) {
        /* Unbind the VBO */
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        /* Unbind the VAO */
        glBindVertexArray(0);
        /* Free verticesBuffer */
        MemoryUtil.memFree(verticesBuffer);
    }

    public void init() {
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(triangleDemo.length);
        verticesBuffer.put(triangleDemo).flip();
        vaoId = createVAO();
        vboId = createVBO();
        insertIntoVBO(verticesBuffer, 0);
        cleanUp(verticesBuffer);
    }
}
