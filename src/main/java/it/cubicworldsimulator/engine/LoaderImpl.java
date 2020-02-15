package it.cubicworldsimulator.engine;

import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;

public class LoaderImpl implements Loader {

    @Override
    public int createVao() {
        int vaoId = GL30C.glGenVertexArrays();
        GL30C.glBindVertexArray(vaoId);
        return vaoId;
    }

    @Override
    public int createVbo() {
        int vboId = glGenBuffers();
        return vboId;
    }

    @Override
    public void insertFloatIntoVbo(int vboId, float[] data, int index, int target, int elementDimension) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        glBindBuffer(target, vboId);
        glBufferData(target, buffer, GL_STATIC_DRAW);
        GL20C.glEnableVertexAttribArray(index);
        GL20C.glVertexAttribPointer(index, elementDimension, GL_FLOAT, false, 0, 0);
    }

    @Override
    public void insertIntIntoVbo(int vboId, int[] data, int index, int target) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        glBindBuffer(target, vboId);
        glBufferData(target, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
