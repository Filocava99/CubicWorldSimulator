package it.cubicworldsimulator.engine.loader;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Material;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class Loader {
    private static final List<Integer> vaoList = new ArrayList<>();
    private static final List<VBO> vboList = new ArrayList<>();
    private static final List<VBO> textureVboList = new ArrayList<>();
    private static final List<VBO> normalsVboList = new ArrayList<>();
    private static final List<FloatBuffer> floatBufferList = new ArrayList<>();
    private static final List<IntBuffer> intBufferlist = new ArrayList<>();

    public static Mesh createMesh(float[] positions, float[] textCoords, int[] indices, float[] normals, Material texture,
                                  float boundingRadius) {
        int vaoId;
        try {
            //Create Vao
            vaoId = createVao();
            vaoList.add(vaoId);

            // Position VBO
            vboList.add(createVbo());
            insertPositionIntoVbo(vboList.get(vboList.size()-1), positions);

            // Index VBO
            vboList.add(createVbo());
            insertIndicesIntoVbo(vboList.get(vboList.size()-1), indices);

            //Texture VBO
            textureVboList.add(createVbo());
            insertTextureIntoVbo(textureVboList.get(textureVboList.size()-1), textCoords);

            //Normals
            normalsVboList.add(createVbo());
            insertNormalsIntoVbo(normalsVboList.get(normalsVboList.size()-1), normals);

        } finally {
            cleanBuffers();
        }
        return new Mesh(texture, boundingRadius, indices.length, vaoId, vboList, textureVboList);
    }

    public static int createVao(){
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        vaoList.add(vaoId);
        return vaoId;
    }

    public static VBO createVbo(){
        VBO myVBO = new VBO(glGenBuffers());
        vboList.add(myVBO);
        return myVBO;
    }

    public static void insertPositionIntoVbo(VBO myVbo, float[] positions) {
        FloatBuffer posBuffer = MemoryUtil.memAllocFloat(positions.length);
        for (Float position : positions) {
            posBuffer.put(position);
        }
        posBuffer.flip();
        insertIntoVbo(posBuffer, myVbo.getId(), 3, 0);
    }

    public static void insertTextureIntoVbo(VBO myVbo, float[] textCoords) {
        FloatBuffer textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
        for (Float textCoord : textCoords) {
            textCoordsBuffer.put(textCoord);
        }
        textCoordsBuffer.flip();
        insertIntoVbo(textCoordsBuffer, myVbo.getId(), 2, 1);
    }

    public static void insertIndicesIntoVbo(VBO myVbo, int[] indices) {
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        for (Integer index : indices) {
            indicesBuffer.put(index);
        }
        indicesBuffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, myVbo.getId());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
    }

    public static void insertNormalsIntoVbo(VBO myVbo, float[] normals) {
        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
        for (Float normal : normals) {
            normalsBuffer.put(normal);
        }
        normalsBuffer.flip();
        insertIntoVbo(normalsBuffer, myVbo.getId(), 3, 2);
    }

    public static void cleanMesh(Mesh mesh) {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        mesh.getVboList().forEach(myVbo -> glDeleteBuffers(myVbo.getId()));

        // Delete the texture VBO
        mesh.getTextureVboList().forEach(myVbo -> glDeleteBuffers(myVbo.getId()));

        // Delete the VAOs
        glBindVertexArray(0);
        glDeleteVertexArrays(mesh.getVaoId());
    }

    public static void cleanBuffers() {
        floatBufferList.forEach(item -> {
            if (item!=null) {
                MemoryUtil.memFree(item);
            }
        });
        intBufferlist.forEach(item -> {
            if (item!=null) {
                MemoryUtil.memFree(item);
            }
        });
    }

    private static void insertIntoVbo(Buffer buffer, int vboId, int size, int index) {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) buffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
    }
}
