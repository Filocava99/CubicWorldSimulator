package it.cubicworldsimulator.engine.loader;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class LoaderUtility {

    public static Vao createVao() {
        Vao myVao = new Vao(glGenVertexArrays());
        glBindVertexArray(myVao.getId());
        return myVao;
    }

    public static Vbo createVbo() {
        return new Vbo(glGenBuffers());
    }

    public static void insertPositionIntoVbo(Vbo myVbo, float[] positions) {
        FloatBuffer posBuffer = MemoryUtil.memAllocFloat(positions.length);
        for (Float position : positions) {
            posBuffer.put(position);
        }
        posBuffer.flip();
        insertIntoVbo(posBuffer, myVbo, 3, 0);
        MemoryUtil.memFree(posBuffer);
    }

    public static void insertTextureIntoVbo(Vbo myVbo, float[] textCoords) {
        FloatBuffer textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
        for (Float textCoord : textCoords) {
            textCoordsBuffer.put(textCoord);
        }
        textCoordsBuffer.flip();
        insertIntoVbo(textCoordsBuffer, myVbo, 2, 1);
        MemoryUtil.memFree(textCoordsBuffer);
    }

    public static void insertIndicesIntoVbo(Vbo myVbo, int[] indices) {
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        for (Integer index : indices) {
            indicesBuffer.put(index);
        }
        indicesBuffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, myVbo.getId());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indicesBuffer);
    }

    public static void insertNormalsIntoVbo(Vbo myVbo, float[] normals) {
        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
        for (Float normal : normals) {

            normalsBuffer.put(normal);
        }
        normalsBuffer.flip();
        insertIntoVbo(normalsBuffer, myVbo, 3, 2);
        MemoryUtil.memFree(normalsBuffer);
    }

    private static void insertIntoVbo(Buffer buffer, Vbo myVbo, int size, int index) {
        glBindBuffer(GL_ARRAY_BUFFER, myVbo.getId());
        glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) buffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
    }
}
