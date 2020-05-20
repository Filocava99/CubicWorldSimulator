package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.*;
import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.game.utility.Pair;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final CommandsQueue commandsQueue;
    private WorldManager worldManager;
    private World world;
    private Scene scene;
    private final Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private final Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();

    private final RendererImpl renderer;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;

    public Game() {
        renderer = new RendererImpl();
        commandsQueue = new CommandsQueue();
    }

    @Override
    public void init(Window window) {
        initShaderPrograms();
        world = new World("test", 463456L);
        worldManager = new WorldManager(world, commandsQueue);
        try {
            SkyBox skyBox = new SkyBox("/models/skybox.obj", "src/main/resources/textures/skybox.png", skyBoxShaderProgram);
            //LIGHTS
            Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
            Vector3f lightColour = new Vector3f(1, 1, 1);
            Vector3f lightPosition = new Vector3f(0, 0, 1);
            float specularPower = 10f;
            float lightIntensity = 1.0f;
            PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
            PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
            pointLight.setAttenuation(att);

            lightPosition = new Vector3f(-1, 0, 0);
            lightColour = new Vector3f(1, 1, 1);
            DirectionalLight directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
            SceneLight sceneLight = new SceneLight(directionalLight, new PointLight[0], new SpotLight[0], ambientLight, specularPower);

            scene = new Scene(opaqueMeshMap, transparentMeshMap, shaderProgram, skyBox, sceneLight);
            
            this.opaqueMeshMap.put(scene.getPlayer().getPlayerModel().getMesh(), List.of(scene.getPlayer().getPlayerModel()));
            
            worldManager.updateActiveChunksSync(new Vector3i(0, 0, 0));
            while (commandsQueue.hasLoadCommand()) {
                Pair<GameItem, GameItem> pair = commandsQueue.runLoadCommand();
                if (pair != null) {
                    logger.trace("Adding chunk mesh");
                    if (pair.hasFirstValue()) {
                        GameItem gameItem = pair.getFirstValue();
                        opaqueMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                    }
                    if (pair.hasSecondValue()) {
                        GameItem gameItem = pair.getSecondValue();
                        transparentMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);

            System.exit(2);
        }
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        scene.getCamera().getCameraMovement().set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            scene.getCamera().getCameraMovement().z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            scene.getCamera().getCameraMovement().z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            scene.getCamera().getCameraMovement().x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            scene.getCamera().getCameraMovement().x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            scene.getCamera().getCameraMovement().y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            scene.getCamera().getCameraMovement().y = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_T)) {
        	scene.getPlayer().changeView(View.THIRDPERSON);
        }else if(window.isKeyPressed(GLFW_KEY_F)) {
        	scene.getPlayer().changeView(View.FIRSTPERSON);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        logger.trace("Updating");
        // Update camera position
        scene.getCamera().movePosition(scene.getCamera().getCameraMovement().x * scene.getCamera().getCameraStep(),
                scene.getCamera().getCameraMovement().y * scene.getCamera().getCameraStep(),
                scene.getCamera().getCameraMovement().z * scene.getCamera().getCameraStep());
        //scene.getPlayer().movePlayer();
        
        // Update scene.getPlayer()() based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplacementVector();
            scene.getCamera().moveRotation(rotVec.x * mouseInput.getMouseSensitivity(), rotVec.y * mouseInput.getMouseSensitivity(), 0);
        }
        if (scene.getPlayer().didPlayerChangedChunk()) {
            worldManager.updateActiveChunksAsync(scene.getPlayer().getChunkPosition());
        }
        
        for (int i = 0; i < 1; i++) {
            Pair<GameItem, GameItem> pair = commandsQueue.runLoadCommand();
            if (pair != null) {
                logger.trace("Adding chunk mesh");
                if (pair.hasFirstValue()) {
                    GameItem gameItem = pair.getFirstValue();
                    opaqueMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                }
                if (pair.hasSecondValue()) {
                    GameItem gameItem = pair.getSecondValue();
                    transparentMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                }
            }
            pair = commandsQueue.runUnloadCommand();
            if (pair != null) {
                if (pair.hasFirstValue()) {
                    GameItem gameItem = pair.getFirstValue();
                    opaqueMeshMap.remove(gameItem.getMesh());
                }
                if (pair.hasSecondValue()) {
                    GameItem gameItem = pair.getSecondValue();
                    transparentMeshMap.remove(gameItem.getMesh());
                }
            }
        }
        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = scene.getSceneLight().getDirectionalLight();
        directionalLight.changeAngle(directionalLight.getAngle() + 1.1f);
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

    private void initSceneShaderProgram() {
        try {
            logger.debug("Creating scene shader program");
            shaderProgram = new ShaderProgram();
            logger.debug("Loading vertex shader");
            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
            logger.debug("Loading fragment shader");
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
            logger.debug("Linking shaders");
            shaderProgram.link();
            shaderProgram.validateProgram();
            // Create uniforms for world and projection matrices
            logger.debug("Creating uniforms");
            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("modelViewMatrix");
            shaderProgram.createUniform("texture_sampler");

            // Create uniform for material
            shaderProgram.createMaterialUniform("material");
            // Create lighting related uniforms
            shaderProgram.createDirectionalLightUniform("directionalLight");
            shaderProgram.createUniform("specularPower");
            shaderProgram.createUniform("ambientLight");
//            shaderProgram.createUniform("MAX_POINT_LIGHTS");
//            shaderProgram.createUniform("MAX_SPOT_LIGHTS");
            shaderProgram.createPointLightListUniform("pointLights", 0);
            shaderProgram.createSpotLightListUniform("spotLights", 0);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            System.exit(3);
        }
    }

    private void initSkyBoxShaderProgram() {
        try {
            logger.debug("Creating skybox shader program");
            skyBoxShaderProgram = new ShaderProgram();
            logger.debug("Loading skybox vertex shader");
            skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/skyBox.vert"));
            logger.debug("Loading skybox fragment shader");
            skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/skyBox.frag"));
            logger.debug("Linking skybox shaders");
            skyBoxShaderProgram.link();
            skyBoxShaderProgram.validateProgram();
            logger.debug("Creating skybox uniforms");
            skyBoxShaderProgram.createUniform("projectionMatrix");
            skyBoxShaderProgram.createUniform("modelViewMatrix");
            skyBoxShaderProgram.createUniform("texture_sampler");
        } catch (Exception e) {
            logger.error(e);
            System.exit(4);
        }
    }
}
