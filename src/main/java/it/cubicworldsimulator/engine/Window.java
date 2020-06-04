package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.game.gui.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final Logger logger = LogManager.getLogger(Window.class);


    private final String title;
    private final boolean fullscreen;
    private final Matrix4f projectionMatrix;
    private final boolean vSync;
    private final boolean debug;
    private final Vector4f clearColor;

    private int width;
    private int height;
    private long windowId;
    private boolean resized;

    public Window(final String title, final Settings mySettings, final Vector4f clearColor) {
        this.title = title;
        this.width = mySettings.getWidth();
        this.height = mySettings.getHeight();
        this.fullscreen = mySettings.getFullscreen();
        this.vSync = mySettings.getvSync();
        this.debug = mySettings.getDebug();
        this.resized = false;
        this.clearColor = clearColor;
        this.projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window won't be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        windowId = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowId == NULL) {
            throw new RuntimeException("Failed to create a GLFW window");
        }
        logger.trace("Window handle: " + windowId);
        glfwSetFramebufferSizeCallback(windowId, (window, width, height) -> {
            //this.width = width;
            //this.height = height;
            this.setResized(false);
        });
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });
        if (fullscreen) {
            glfwMaximizeWindow(windowId);
        } else {
            GLFWVidMode monitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
            Objects.requireNonNull(monitor);
            glfwSetWindowPos(
                    windowId,
                    (monitor.width() - width) / 2,
                    (monitor.height() - height) / 2
            );
        }
        glfwMakeContextCurrent(windowId);
        if (isvSync()) {
            glfwSwapInterval(1);
        }
        glfwShowWindow(windowId);
        GL.createCapabilities();
        setClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glEnable(GL_DEPTH_TEST);
        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        if(debug){
            glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        }
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowId, keyCode) == GLFW_PRESS;
    }
    
    public boolean isKeyReleased(int keyCode) {
        return glfwGetKey(windowId, keyCode) == GLFW_RELEASE;
    }
    
    public boolean onKeyReleased(int keyCode) {
    	return !isKeyPressed(keyCode) && !isKeyReleased(keyCode);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    public long getWindowId() {
        return windowId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void update() {
        logger.trace("Updating");
        if(isResized()){
            glViewport(0, 0, getWidth(), getHeight());
            setResized(false);
        }
        glfwSwapBuffers(windowId);
        glfwPollEvents();
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / (float) height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void restoreState() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
}
