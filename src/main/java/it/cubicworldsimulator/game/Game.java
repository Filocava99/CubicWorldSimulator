package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.*;
import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.game.gui.Settings;
import it.cubicworldsimulator.game.utility.Pair;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

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
    private final Settings mySettings;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;
    private Window window;
    private final Timer timer = new Timer();
    private static final float DELTA_NIGHT = 0.00250f;
    private long time = 0;
    private final Vector4f newColour = new Vector4f(0.0f, 0.0f, 0.2f, 1.0f);

    public Game(Settings mySettings) {
        renderer = new RendererImpl();
        commandsQueue = new CommandsQueue();
        this.mySettings = mySettings;
    }

    @Override
    public void init(Window window) {
        initShaderPrograms();
        this.window=window;
        world = new World(mySettings.getWorldName(),
                mySettings.getWorldSeed());
        worldManager = new WorldManager(world, commandsQueue);
        try {
            SkyBox skyBox = new SkyBox("/models/skybox.obj", "src/main/resources/textures/skybox.png", skyBoxShaderProgram);
            //LIGHTS
            Vector3f ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
            Vector3f lightColour = new Vector3f(1, 1, 1);
            Vector3f lightPosition = new Vector3f(0, 0, 1);
            float specularPower = 5f;
            float lightIntensity = 1.0f;
            PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
            PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
            pointLight.setAttenuation(att);
            lightPosition = new Vector3f(-1, 0, 0);
            lightColour = new Vector3f(1, 1, 1);
            DirectionalLight directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
            SceneLight sceneLight = new SceneLight(directionalLight, new PointLight[0], new SpotLight[0], ambientLight, specularPower);

            scene = new Scene(opaqueMeshMap, transparentMeshMap, shaderProgram, skyBox, sceneLight);
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
        scene.getPlayer().getCameraMovement().set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            scene.getPlayer().getCameraMovement().z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            scene.getPlayer().getCameraMovement().z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            scene.getPlayer().getCameraMovement().x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            scene.getPlayer().getCameraMovement().x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            scene.getPlayer().getCameraMovement().y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            scene.getPlayer().getCameraMovement().y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        logger.trace("Updating");
        // Update camera position
        scene.getPlayer().movePosition(scene.getPlayer().getCameraMovement().x * scene.getPlayer().getCameraStep(),
                scene.getPlayer().getCameraMovement().y * scene.getPlayer().getCameraStep(),
                scene.getPlayer().getCameraMovement().z * scene.getPlayer().getCameraStep());

        // Update scene.getPlayer()() based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplacementVector();
            scene.getPlayer().moveRotation(rotVec.x * mouseInput.getMouseSensitivity(), rotVec.y * mouseInput.getMouseSensitivity(), 0);
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
        dayNight();
        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = scene.getSceneLight().getDirectionalLight();
        directionalLight.changeAngle(directionalLight.getAngle() + 1.1f);
    }

    private void dayNight() {
        time += timer.getElapsedTime()*1000;
        time %= 24_000;
        if(time >= 0 && time < 5000){
            if ((newColour.z -= DELTA_NIGHT) < 0) {
                newColour.z=0;
                return;
            } else {
                newColour.z -= DELTA_NIGHT;
            }
        }else if(time >= 5000 && time < 21000){
            if ((newColour.z += DELTA_NIGHT) > 1) {
                newColour.z=1;
                return;
            } else {
                newColour.z += DELTA_NIGHT;
            }
        }else{
            if ((newColour.z -= DELTA_NIGHT) < 0) {
                newColour.z=0;
                return;
            } else {
                newColour.z -= DELTA_NIGHT;
            }
        }
        window.setClearColor(newColour.x, newColour.y, newColour.z, newColour.w);
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
            skyBoxShaderProgram.createUniform("ambientLight");
        } catch (Exception e) {
            logger.error(e);
            System.exit(4);
        }
    }
}
