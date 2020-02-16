package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private int direction = 0;

    private float color = 0.0f;

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;

    private Scene scene;

    private final RendererImpl renderer;
    private ShaderProgram shaderProgram;

    public Game() {
        renderer = new RendererImpl();
    }

    @Override
    public void init(Window window) throws Exception {
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().setPerspective(FOV, aspectRatio,
                Z_NEAR, Z_FAR);
        WorldManager worldManager = new WorldManager(new World("test", 4242L));
        GameItem gameItem = new GameItem(worldManager.mesh);
        gameItem.setPosition(0, 0, -10);
        Map<Mesh, List<GameItem>> meshMap = new HashMap<>();
        meshMap.put(worldManager.mesh, List.of(gameItem));
        initShaderPrograms();
        scene = new Scene(meshMap, shaderProgram);
    }

    @Override
    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(scene, window.getWidth(), window.getHeight());
    }

    @Override
    public void cleanUp() {

    }

    private void initShaderPrograms() {
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
            shaderProgram.link();

            // Create uniforms for world and projection matrices
            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("worldMatrix");
            shaderProgram.createUniform("texture_sampler");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
