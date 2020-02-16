package it.cubicworldsimulator.engine.loader;

import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class LoaderImpl implements Loader {

    private final List<FloatBuffer> floatBuffersList;
    private final List<IntBuffer> intBuffersList;

    public LoaderImpl() {
        this.floatBuffersList = new ArrayList<>();
        this.intBuffersList = new ArrayList<>();
    }

    @Override
    public int createVao() {
        int vaoId = GL30C.glGenVertexArrays();
        GL30C.glBindVertexArray(vaoId);
        return vaoId;
    }

    @Override
    public int createVbo() {
        return glGenBuffers();
    }

    @Override
    public void insertDataIntoVbo(int vboId, float[] data, int index, int target, int elementDimension) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        this.floatBuffersList.add(buffer);
        buffer.put(data).flip();
        glBindBuffer(target, vboId);
        glBufferData(target, buffer, GL_STATIC_DRAW);
        GL20C.glEnableVertexAttribArray(index);
        GL20C.glVertexAttribPointer(index, elementDimension, GL_FLOAT, false, 0, 0);
    }

    @Override
    public void insertIndices(int vboId, int[] indices, int indexToBind, int target) {
        IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);
        this.intBuffersList.add(buffer);
        buffer.put(indices).flip();
        glBindBuffer(target, vboId);
        glBufferData(target, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(indexToBind);
    }

    public void cleanUp() {
        this.floatBuffersList.removeIf(value -> true);
        this.intBuffersList.removeIf(value -> true);
    }
}
