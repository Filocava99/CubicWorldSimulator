package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.hud.GenericHud;
import it.cubicworldsimulator.engine.hud.UpperHud;
import it.cubicworldsimulator.engine.graphic.*;

import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.engine.graphic.light.*;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final CommandsQueue commandsQueue;
    private WorldManager worldManager;
    private Scene scene;
    private final Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private final Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();

    private final RendererImpl renderer;
    private final Settings mySettings;
    private final GenericHud upperHud;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;
    private LightCycleManager dayNightManager;

    public Game(Settings mySettings) {
        renderer = new RendererImpl();
        commandsQueue = new CommandsQueue();
        this.mySettings = mySettings;
        this.upperHud = new UpperHud();
    }

    @Override
    public void init(Window window) {
        initShaderPrograms();
        try {
            upperHud.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        World world = new World(mySettings.getWorldName(),
                mySettings.getWorldSeed());
        worldManager = new WorldManager(world, commandsQueue);
        try {
            SkyBox skyBox = new SkyBox("/models/skybox.obj", "textures/skybox.png", skyBoxShaderProgram);
            //LIGHTS
            Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.2f);
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

            SceneLight sceneLight = new SceneLight.Builder()
            		.addDirectionalLight(directionalLight)
            		.addAmbientLight(ambientLight)
            		.addSpecularPower(specularPower)
            		.build();

            dayNightManager = new DayNightManager(sceneLight, upperHud, mySettings.getDaySpeed());
            dayNightManager.setDelta(0.00250f);
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
        scene.getCamera().getCameraMovement().set(0, 0, 0);
        //Player movement
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
        
        //Change visual 
        if(window.isKeyPressed(GLFW_KEY_T)) {
        	this.opaqueMeshMap.put(this.scene.getPlayerModel().getMesh(), List.of(this.scene.getPlayerModel()));
        	scene.getCamera().setStrategy((p,r)->{
        		Vector3f newPosition = new Vector3f(p);
        		newPosition.x += (float)Math.sin(Math.toRadians(r.y)) * -1.0f * Constants.DISTANCE_FROM_CAMERA;
                newPosition.z += (float)Math.cos(Math.toRadians(r.y)) * Constants.DISTANCE_FROM_CAMERA;
                newPosition.y += Constants.DISTANCE_FROM_CAMERA / 2;
        		return newPosition;
        	});
        }else if(window.isKeyPressed(GLFW_KEY_F)) {
        	this.opaqueMeshMap.remove(this.scene.getPlayerModel().getMesh(), List.of(this.scene.getPlayerModel()));
        	scene.getCamera().setStrategy( (p,r) -> {
        		Vector3f newPosition = new Vector3f(p);
        		return newPosition;
        	});
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        logger.trace("Updating");
        // Update camera position
        scene.getPlayer().movePosition(scene.getCamera().getCameraMovement().x * scene.getCamera().getCameraStep(),
                scene.getCamera().getCameraMovement().y * scene.getCamera().getCameraStep(),
                scene.getCamera().getCameraMovement().z * scene.getCamera().getCameraStep());
       
        
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
        dayNightManager.updateCycle();
        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = scene.getSceneLight().getDirectionalLight();
        directionalLight.changeAngle(directionalLight.getAngle() + 1.1f);
    }

    @Override
    public void render(Window window) {
        logger.trace("Rendering");
        renderer.render(scene, window);
        upperHud.renderHud(window);
    }

    @Override
    public void cleanUp() {
        scene.cleanUp();
        if (upperHud != null) {
            upperHud.cleanup();
        }
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
