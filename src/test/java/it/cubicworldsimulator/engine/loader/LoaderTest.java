package it.cubicworldsimulator.engine.loader;

import exceptions.GLComponentException;
import exceptions.InsertDataException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

class LoaderTest {

    OpenGLComponent.Vao vao;

    @BeforeAll
    public static void initTest() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        long window = glfwCreateWindow(100, 100, "TEST", NULL, NULL);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @Test
    public void createVao() throws GLComponentException {
        try {
            vao = LoaderUtility.createVao();
        } catch (Exception e) {
            throw new GLComponentException("Error creating Vao");
        }
    }

    @Test
    public void checkInsertFloat() {
        OpenGLComponent.Vbo vbo = null;
        float[] floatArray = new float[20];
        try {
            vbo = createVbo();
        } catch (GLComponentException e){
            fail("Failed to create GLComponent: " + e.getMessage());
        }
        try {
            insertFloatArrayIntoVbo(vbo, floatArray);
        } catch (InsertDataException e){
            fail("Failed to insert data: " + e.getMessage());
        }
        FloatBuffer floatBuffer = Objects.requireNonNull(
                                glMapBuffer(GL_ARRAY_BUFFER, GL_READ_ONLY, null))
                                .asFloatBuffer();
        glUnmapBuffer(GL_ARRAY_BUFFER);
        assertNotNull(floatBuffer);
        IntStream.range(0, floatArray.length).forEach(i -> {
                    assertEquals(floatArray[i], floatBuffer.get(i));
        });
    }

    @Test
    public void checkInsertInt() {
        OpenGLComponent.Vbo vbo = null;
        int[] intArray = new int[20];
        try {
            vbo = createVbo();
        } catch (GLComponentException e){
            fail("Failed to create GLComponent: " + e.getMessage());
        }
        try {
            insertIntArrayIntoVbo(vbo, intArray);
        } catch (InsertDataException e){
            fail("Failed to insert data: " + e.getMessage());
        }
        IntBuffer intBuffer = Objects.requireNonNull(
                              glMapBuffer(GL_ARRAY_BUFFER, GL_READ_ONLY, null))
                              .asIntBuffer();
        glUnmapBuffer(GL_ARRAY_BUFFER);
        assertNotNull(intBuffer);
        IntStream.range(0, intArray.length).forEach(i -> {
            assertEquals(intArray[i], intBuffer.get(i));
        });
    }


    public OpenGLComponent.Vbo createVbo() throws GLComponentException {
        OpenGLComponent.Vbo myVbo = null;
        try {
            myVbo = LoaderUtility.createVbo();
        } catch (Exception e) {
            throw new GLComponentException("Error creating Vbo");
        }
        return myVbo;
    }

    public void insertFloatArrayIntoVbo(OpenGLComponent.Vbo vbo, float[] floatArray) throws InsertDataException {
        try {
            LoaderUtility.insertPositionIntoVbo(vbo, floatArray);
        }
        catch (Exception e){
            throw new InsertDataException("float array into VBO " + vbo.getId());
        }
    }

    public void insertIntArrayIntoVbo(OpenGLComponent.Vbo vbo, int[] intArray) throws InsertDataException {
        try {
            LoaderUtility.insertIndicesIntoVbo(vbo, intArray);
        }
        catch (Exception e){
            throw new InsertDataException("int array into VBO " + vbo.getId());
        }
    }
}