package it.cubicworldsimulator.engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class LoaderImpl implements Loader {

    private int vaoId;
    private int vboId;
    private FloatBuffer verticesBuffer;
    private ShaderProgram shaderProgram;

    //TODO make this a generic loader
    private final float[] triangleDemo = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    public final void init(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        this.verticesBuffer = MemoryUtil.memAllocFloat(triangleDemo.length);
        this.verticesBuffer.put(triangleDemo).flip();
        this.vaoId = createVAO();
        this.vboId = createVBO();
        insertIntoVBO();
    }

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

    private void insertIntoVBO() {
        /* Insert vertices in VBO */
        glBufferData(GL_ARRAY_BUFFER,
                verticesBuffer,
                GL_STATIC_DRAW);
        /* Mark attribute index 0 with vertex */
        glEnableVertexAttribArray(0);
        /* Mark data */
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    private void clean() {
        if (this.shaderProgram != null) {
            this.shaderProgram.cleanup();
        }
        glDisableVertexAttribArray(0);
        /* Unbind the VBO */
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vboId);
        /* Unbind the VAO */
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vaoId);
        /* Free verticesBuffer */
        MemoryUtil.memFree(this.verticesBuffer);
    }
}
