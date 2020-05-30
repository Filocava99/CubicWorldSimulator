package it.cubicworldsimulator.engine.loader;

import exceptions.GLComponentException;
import exceptions.InsertDataException;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.gui.FrameProperty;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11C.glGetError;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

class LoaderTest {

    OpenGLComponent.Vao vao;

    public void createVao() throws GLComponentException {
        try {
            vao = LoaderUtility.createVao();
        } catch (Exception e) {
            throw new GLComponentException("Error creating Vao");
        }
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

    @Test
    public void checkInsertFloat() {
        initTest();
        OpenGLComponent.Vbo vbo = null;
        float[] floatArray = new float[20];
        for (int i=0; i<floatArray.length; i++) {
            floatArray[i] = i;
        }
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
        ByteBuffer buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_READ_ONLY, null);
        glUnmapBuffer(vbo.getId());
        if (buffer==null) {
            fail("Null buffer");
        }
        FloatBuffer floatBuffer  = buffer.asFloatBuffer();
        IntStream.range(0, floatArray.length).forEach(i -> {
                    assertEquals(floatArray[i], floatBuffer.get(i));
        });
    }
    

    public void initTest() {
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
}