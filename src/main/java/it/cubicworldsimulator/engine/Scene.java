package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Player;
import it.cubicworldsimulator.engine.graphic.PlayerModel;
import it.cubicworldsimulator.engine.graphic.SkyBox;

import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.loader.OBJLoader;

import java.util.*;

import org.joml.Vector3f;

public class Scene {

    private Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();
    private final ShaderProgram shaderProgram;
    private final SkyBox skyBox;
    private final SceneLight sceneLight;
    private final Player player = new Player( new Vector3f(0,35,0));
    private final PlayerModel playerModel = new PlayerModel();
    private final Camera camera = new Camera(new Vector3f(0,35,0));

    /**
     * @param opaqueMeshMap map of meshes with opaque textures
     * @param transparentMeshMap map of meshes with transparent textures
     * @param shaderProgram main shader program
     * @param skyBox skybox instance to wrap the world in it
     * @param sceneLight sceneLight instance to be used to light up the scene meshes
     */
    public Scene(Map<Mesh, List<GameItem>> opaqueMeshMap, Map<Mesh, List<GameItem>> transparentMeshMap, ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight) {
        this.opaqueMeshMap = opaqueMeshMap;
        this.transparentMeshMap = transparentMeshMap;
        this.shaderProgram = shaderProgram;
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
        loadPlayerModel();
        this.player.attach(this.camera);
        this.player.attach(this.playerModel);
    }

    /**
     * @param shaderProgram main shader program
     * @param skyBox skybox instance to wrap the world in it
     * @param sceneLight sceneLight instance to be used to light up the scene meshes
     * @param gameItems list of game items with opaque textures
     */
    public Scene(ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
    }

    /**
     * @param gameItems array of game items with opaque textures
     * @param shaderProgram main shader program
     * @param skyBox skybox instance to wrap the world in it
     * @param sceneLight sceneLight instance to be used to light up the scene meshes
     */
    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
    }
    
    public Camera getCamera() {
    	return this.camera;
    }

    /**
     * Set the opaque mesh map
     * @param gameItems arrays of game items with opaque textures
     */
    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = opaqueMeshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                opaqueMeshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    /**
     * Frees the GPU memory from every loaded mesh and shader programs
     */
    public void cleanUp(){
        opaqueMeshMap.keySet().forEach(Mesh::cleanMesh);
        transparentMeshMap.keySet().forEach(Mesh::cleanMesh);
        shaderProgram.cleanup();
        skyBox.getMesh().cleanMesh();
        skyBox.getShaderProgram().cleanup();
    }

    /**
     * Returns the skybox instance
     * @return the skybox instance
     */
    public SkyBox getSkyBox() {
        return skyBox;
    }

    /**
     * Returns the sceneLight instance
     * @return the sceneLight instance
     */
    public SceneLight getSceneLight() {
    	return this.sceneLight;
    }

    /**
     * Returns the playerModel instance
     * @return playerModel
     */
    public PlayerModel getPlayerModel() {
    	return this.playerModel;
    }

    /**
     * Returns the main shader program
     * @return the main shader program
     */
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    /**
     * Returns the map of meshes with opaque textures
     * @return the map of meshes with opaque textures
     */
    public Map<Mesh, List<GameItem>> getOpaqueMeshMap() {
        return Collections.unmodifiableMap(opaqueMeshMap);
    }

    /**
     * Returns the map of meshes with transparent textures
     * @return the map of meshes with transparent textures
     */
    public Map<Mesh, List<GameItem>> getTransparentMeshMap() {
        return Collections.unmodifiableMap(transparentMeshMap);
    }

    /**
     * Returns the player instance
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }

    private void loadPlayerModel() {
        OBJLoader objLoader = new OBJLoader();
        try {
            Mesh playerMesh = objLoader.loadFromOBJ("/models/person.obj", "textures/playerTexture.png");
            this.playerModel.setMesh(playerMesh);
            this.playerModel.setPosition(new Vector3f(this.player.getPosition()));
            this.playerModel.setRotation(new Vector3f(0,180,0));
            this.playerModel.setScale(0.06f);
            this.playerModel.setIgnoreFrustum(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
