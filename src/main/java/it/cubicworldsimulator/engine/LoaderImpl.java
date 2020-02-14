package it.cubicworldsimulator.engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LoaderImpl implements Loader {
    @Override
    public int createVao() {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        return vaoId;
    }

    @Override
    public int createVbo() {
        return glGenBuffers();
    }

    @Override
    public void insertFloatIntoVbo(float[] data, int index) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        this.activateVbo(index);
    }

    @Override
    public void insertIntIntoVbo(int[] data, int index) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        this.activateVbo(index);
    }

    @Override
    public void activateVbo(int index) {
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, 3, GL_FLOAT, false, 0, 0);
    }
}
