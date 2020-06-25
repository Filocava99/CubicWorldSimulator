package it.cubicworldsimulator.engine.loader;

import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

/**
 * Utility class for loading data in memory
 * @author Lorenzo Balzani
 */
public class LoaderUtility {
    /**
     * @return the created Vao
     */
    public static Vao createVao() {
        Vao myVao = new Vao(glGenVertexArrays());
        glBindVertexArray(myVao.getId());
        return myVao;
    }

    /**
     * @return the created Vbo
     */
    public static Vbo createVbo() {
        return new Vbo(glGenBuffers());
    }

    /**
     * It inserts a float array (in this case position) into a Vbo
     * @param myVbo the given Vbo
     * @param positions float array
     */
    public static void insertPositionIntoVbo(Vbo myVbo, float[] positions) {
        FloatBuffer posBuffer = MemoryUtil.memAllocFloat(positions.length);
        for (Float position : positions) {
            posBuffer.put(position);
        }
        posBuffer.flip();
        insertIntoVbo(posBuffer, myVbo, 3, 0);
        MemoryUtil.memFree(posBuffer);
    }

    /**
     * It inserts a float array (in this case textCoords) into a Vbo
     * @param myVbo the given Vbo
     * @param textCoords float array
     */
    public static void insertTextureIntoVbo(Vbo myVbo, float[] textCoords) {
        FloatBuffer textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
        for (Float textCoord : textCoords) {
            textCoordsBuffer.put(textCoord);
        }
        textCoordsBuffer.flip();
        insertIntoVbo(textCoordsBuffer, myVbo, 2, 1);
        MemoryUtil.memFree(textCoordsBuffer);
    }

    /**
     * It inserts a int array (in this case indices) into a Vbo
     * @param myVbo the given Vbo
     * @param indices float array
     */
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

    /**
     * It inserts a float array (in this case normals) into a Vbo
     * @param myVbo the given Vbo
     * @param normals float array
     */
    public static void insertNormalsIntoVbo(Vbo myVbo, float[] normals) {
        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
        for (Float normal : normals) {
            normalsBuffer.put(normal);
        }
        normalsBuffer.flip();
        insertIntoVbo(normalsBuffer, myVbo, 3, 2);
        MemoryUtil.memFree(normalsBuffer);
    }

    /**
     * It inserts a generic Buffer into a Vbo
     * @param buffer the buffer in which we have previously loaded data.
     * @param myVbo the given Vbo
     * @param size size of whole array
     * @param index from the method has to load data
     */
    private static void insertIntoVbo(Buffer buffer, Vbo myVbo, int size, int index) {
        glBindBuffer(GL_ARRAY_BUFFER, myVbo.getId());
        glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) buffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
    }

    /**
     * @param resource path to the resource
     * @param bufferSize
     * @return ByteBuffer with loaded resource
     * @throws IOException
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = LoaderUtility.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);
                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }
        buffer.flip();
        return buffer;
    }

    /**
     * @param buffer the given buffer
     * @param newCapacity new capacity in byte
     * @return a new resized buffer
     */
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
