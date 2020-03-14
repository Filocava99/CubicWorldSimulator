package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.*;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
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
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Camera camera; //TODO Va messa nella scene direttamente?
    private final Player player;
    private final CommandsQueue commandsQueue;
    private WorldManager worldManager;
    private World world;
    private Scene scene;
//<<<<<<< HEAD
  //  private final Map<Mesh, List<GameItem>> meshMap = new HashMap<>();
//=======
    private final Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private final Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();

//>>>>>>> cava
    private final RendererImpl renderer;
    private ShaderProgram shaderProgram;
    private ShaderProgram skyBoxShaderProgram;
    private float lightAngle;
    private float spotAngle = 0;
    private float spotInc = 1;

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
            //TODO Creare una copia della instanza opaqueMeshMap
            scene = new Scene(opaqueMeshMap, transparentMeshMap,shaderProgram, skyBox, camera);
            worldManager.updateActiveChunksSync(new Vector3i(0,0,0));
            while(commandsQueue.hasLoadCommand()){
                GameItem[] chunks = commandsQueue.runLoadCommand();
                if(chunks != null){
                    logger.trace("Adding chunk mesh");
                    if(chunks[0] != null){
                        GameItem gameItem = chunks[0];
                        opaqueMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                    }
                    if(chunks[1] != null){
                        GameItem gameItem = chunks[1];
                        transparentMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        
        setUpLights();
        //TODO forse qua va settata la posizione della camera
    }
    
    private void setUpLights() {
    	SceneLight sceneLight = new SceneLight();
    	sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));
    	float lightIntensity = 1.0f;
    	Vector3f lightPosition = new Vector3f(-1, 0, 0);
    	Vector3f lightColor = new Vector3f(1, 1, 1);
    	sceneLight.setDirectionalLight(new DirectionalLight(lightColor, lightPosition, lightIntensity));
    	this.scene.setSceneLight(sceneLight);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {

        camera.getCameraMovement().set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W) || (window.isKeyPressed(GLFW_KEY_UP))) {
            camera.getCameraMovement().z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S) || (window.isKeyPressed(GLFW_KEY_DOWN))) {
            camera.getCameraMovement().z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A) || (window.isKeyPressed(GLFW_KEY_LEFT))) {
            camera.getCameraMovement().x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D) || (window.isKeyPressed(GLFW_KEY_RIGHT))) {
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
      
        updateLights();
        
        if(player.didPlayerChangedChunk()){
            worldManager.updateActiveChunksAsync(player.getChunkPosition());
        }
        for(int i = 0; i < 1; i++){
            GameItem[] chunks = commandsQueue.runLoadCommand();
            if(chunks != null){
                if(chunks[0] != null){
                    GameItem gameItem = chunks[0];
                    opaqueMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                }
                if(chunks[1] != null){
                    GameItem gameItem = chunks[1];
                    transparentMeshMap.put(gameItem.getMesh(), List.of(gameItem));
                }
            }
            chunks = commandsQueue.runUnloadCommand();
            if(chunks != null){
                if(chunks[0] != null){
                    GameItem gameItem = chunks[0];
                    opaqueMeshMap.remove(gameItem.getMesh());
                }
                if(chunks[1] != null){
                    GameItem gameItem = chunks[1];
                    transparentMeshMap.remove(gameItem.getMesh());
                }
            }
        }
        
        
    }
    
    private void updateLights() {
        SceneLight sceneLight = this.scene.getSceneLight();
        
        //Update directional light
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        this.lightAngle = this.lightAngle + 1.1f;
        if(this.lightAngle > 90) {
        	directionalLight.setIntensity(0);
        	if(this.lightAngle >= 360) {
        		this.lightAngle = -90;
        	}
        	sceneLight.getAmbientLight().set(0.3f, 0.3f, 0.3f);
        }else if( this.lightAngle <= -80 || this.lightAngle >= 80) {
        	float factor = 1 - (float) (Math.abs(this.lightAngle) - 80) / 10.0f;
        	sceneLight.getAmbientLight().set(factor, factor, factor);
        	directionalLight.setIntensity(factor);
        	directionalLight.getColor().y = Math.max(factor, 0.9f);
        	directionalLight.getColor().z = Math.max(factor, 0.5f);
        }else {
        	sceneLight.getAmbientLight().set(1, 1, 1);
        	directionalLight.setIntensity(1);
        	directionalLight.getColor().x = 1;
        	directionalLight.getColor().y = 1;
        	directionalLight.getColor().z = 1;
        }
        
        double angleRadians = Math.toRadians(this.lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angleRadians);
        directionalLight.getDirection().y = (float) Math.cos(angleRadians);
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
            
            //AGGIUNTA PELATINI
            shaderProgram.createMateriaUniform("material");
            shaderProgram.createUniform("specularPower");
            shaderProgram.createUniform("ambientLight");
            shaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
            shaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
            shaderProgram.createDirectionalLightUnform("directionalLight");
            
        } catch (Exception e) {
            logger.error(e.getMessage());
            //logger.error(e.getStackTrace().toString());
            e.printStackTrace();
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
