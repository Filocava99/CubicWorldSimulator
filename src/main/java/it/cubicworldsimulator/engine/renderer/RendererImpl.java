package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.Mesh;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class RendererImpl implements Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    /* TODO pasare i nomi delle shader come argomenti del costruttore? Credo sia effettivamente meglio. Fare una interfaccia per il renderer? In questo modo non occorre passare le shader per argomento */
    public RendererImpl() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.frag"));
        shaderProgram.link();

        // Create uniforms for world and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Scene scene) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        shaderProgram.setUniform("texture_sampler", 0);

        // Render each gameItem
        for (Mesh mesh : scene.getMeshMap().keySet()) {
            //TODO Mi serve la parte delle luci per completare
            mesh.renderList(scene.getMeshMap().get(mesh), (GameItem gameItem) -> {});
        }
        shaderProgram.unbind();
    }

    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}