package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Player;
import it.cubicworldsimulator.engine.graphic.SkyBox;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import it.cubicworldsimulator.game.world.chunk.Chunk;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Game implements GameLogic {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private int direction = 0;

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Camera camera; //TODO Va messa nella scene direttamente?
    private final Player player;
    private WorldManager worldManager;
    private World world;
    private Scene scene;

    private final RendererImpl renderer;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;

    //TODO Ha senso il metodo init()? Se ha senso conviene chiamarlo a parte oppure direttamente dal costruttore?
    public Game() {
        renderer = new RendererImpl();
        camera = new Camera();
        player = new Player(camera);
    }

    @Override
    public void init(Window window){

        world = new World("test", 424243563456L);
        worldManager = new WorldManager(world);
        worldManager.renderChunkColumn(new Vector2f(0,0));
        worldManager.renderChunkColumn(new Vector2f(0,1));
        worldManager.renderChunkColumn(new Vector2f(1,0));
        worldManager.renderChunkColumn(new Vector2f(1,1));

        List<GameItem> gameItems = new ArrayList<>();

        //TODO Da fare meglio
        for(ChunkMesh chunkMesh : worldManager.getActiveMeshes()){
            chunkMesh.buildMesh();
            GameItem gameItem = new GameItem(chunkMesh.getMesh());
            gameItem.setPosition(chunkMesh.getChunk().getPosition());
            gameItems.add(gameItem);
        }

        initShaderPrograms();
        try {
            SkyBox skyBox = new SkyBox("/models/skybox.obj", "src/main/resources/textures/skybox.png", skyBoxShaderProgram);
            scene = new Scene(gameItems.toArray(GameItem[]::new), shaderProgram, skyBox, camera);
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
            System.exit(2);
        }
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
        logger.trace("Updating");
        if(player.didPlayerChangedChunk()){
            worldManager.updateActiveChunks(player.getChunkPosition());
        }
    }

    @Override
    public void render(Window window) {
        logger.trace("Rendering");
        renderer.render(scene, window.getWidth(), window.getHeight());
    }

    @Override
    public void cleanUp() {
        scene.cleanUp();
    }

    private void initShaderPrograms() {
        logger.debug("Initializing shader programs");
        initSceneShaderProgram();
        initSkyBoxShaderProgram();
    }

    private void initSceneShaderProgram(){
        try {
            logger.debug("Creating scene shader program");
            shaderProgram = new ShaderProgram();
            logger.debug("Loading vertex shader");
            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
            logger.debug("Loading fragment shader");
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
            logger.debug("Linking shaders");
            shaderProgram.link();
            // Create uniforms for world and projection matrices
            logger.debug("Creating uniforms");
            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("modelViewMatrix");
            shaderProgram.createUniform("texture_sampler");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
            System.exit(3);
        }
    }

    private void initSkyBoxShaderProgram(){
        try {
            logger.debug("Creating skybox shader program");
            skyBoxShaderProgram = new ShaderProgram();
            logger.debug("Loading skybox vertex shader");
            skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/skyBox.vert"));
            logger.debug("Loading skybox fragment shader");
            skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/skyBox.frag"));
            logger.debug("Linking skybox shaders");
            skyBoxShaderProgram.link();
            logger.debug("Creating skybox uniforms");
            skyBoxShaderProgram.createUniform("projectionMatrix");
            skyBoxShaderProgram.createUniform("modelViewMatrix");
            skyBoxShaderProgram.createUniform("texture_sampler");
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
            System.exit(4);
        }
    }
}
