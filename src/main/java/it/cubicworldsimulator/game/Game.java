package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.*;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final Camera camera; //TODO Va messa nella scene direttamente?
    private final Player player;
    private final CommandsQueue commandsQueue;
    private WorldManager worldManager;
    private World world;
    private Scene scene;
    private final Map<Mesh, List<GameItem>> meshMap = new HashMap<>();

    private final RendererImpl renderer;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;

    //TODO Ha senso il metodo init()? Se ha senso conviene chiamarlo a parte oppure direttamente dal costruttore?
    public Game() {
        renderer = new RendererImpl();
        camera = new Camera();
        player = new Player(camera);
        commandsQueue = new CommandsQueue();
    }

    @Override
    public void init(Window window){
        world = new World("test", 463456L);
        worldManager = new WorldManager(world, commandsQueue);
        initShaderPrograms();
        try {
            SkyBox skyBox = new SkyBox("/models/skybox.obj", "src/main/resources/textures/skybox.png", skyBoxShaderProgram);
            scene = new Scene(meshMap, shaderProgram, skyBox, camera);
            worldManager.updateActiveChunksSync(new Vector3i(0,0,0));
            while(commandsQueue.hasLoadCommand()){
                GameItem chunk = commandsQueue.runLoadCommand();
                if(chunk != null){
                    logger.debug("Adding chunk mesh");
                    meshMap.put(chunk.getMesh(), List.of(chunk));
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
            System.exit(2);
        }
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {

        camera.getCameraMovement().set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.getCameraMovement().z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.getCameraMovement().z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.getCameraMovement().x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.getCameraMovement().x = 1;

        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.getCameraMovement().y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.getCameraMovement().y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        logger.trace("Updating");
        // Update camera position
        camera.movePosition(camera.getCameraMovement().x * camera.getCameraStep(),
                camera.getCameraMovement().y * camera.getCameraStep(),
                camera.getCameraMovement().z * camera.getCameraStep());

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplacementVector();
            camera.moveRotation(rotVec.x * mouseInput.getMouseSensitivity(), rotVec.y * mouseInput.getMouseSensitivity(), 0);
        }
        if(player.didPlayerChangedChunk()){
            worldManager.updateActiveChunksAsync(player.getChunkPosition());
        }
        for(int i = 0; i < 1; i++){
            GameItem chunk = commandsQueue.runLoadCommand();
            if(chunk != null){
                logger.debug("Adding chunk mesh");
                meshMap.put(chunk.getMesh(), List.of(chunk));
            }
            chunk = commandsQueue.runUnloadCommand();
            if(chunk != null){
                //logger.debug("Removing chunk mesh");
                //logger.debug(chunk.getMesh() == null);
                var list = meshMap.remove(chunk.getMesh());
                //logger.debug(list == null);
            }
        }
    }

    @Override
    public void render(Window window) {
        logger.trace("Rendering");
        renderer.render(scene, window);
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
