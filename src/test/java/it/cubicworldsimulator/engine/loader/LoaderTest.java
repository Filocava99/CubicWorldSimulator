package it.cubicworldsimulator.engine.loader;

import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

class LoaderTest {

    @Test
    public void createVao() {
        initTest();
        try {
            LoaderUtility.createVao();
        }
        catch (Exception e){
            fail("Failed to create VAO");
        }
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