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

    public Scene(ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
        /*this.player.attach(this.camera);
        loadPlayerModel();*/
    }
    
   
    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
        /*this.player.attach(this.camera);
        loadPlayerModel();*/
    }
    
    private void loadPlayerModel() {
    	OBJLoader objLoader = new OBJLoader();
		try {
			Mesh playerMesh = objLoader.loadFromOBJ("/models/person.obj", "src/main/resources/textures/playerTexture.png");
			this.playerModel.setMesh(playerMesh); 
			this.playerModel.setPosition(new Vector3f(this.player.getPosition()));
			this.playerModel.setScale(0.1f);
			this.playerModel.setIgnoreFrustum(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public Camera getCamera() {
    	return this.camera;
    }

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

    public void cleanUp(){
        opaqueMeshMap.keySet().forEach(Mesh::cleanMesh);
        transparentMeshMap.keySet().forEach(Mesh::cleanMesh);
        shaderProgram.cleanup();
        skyBox.getMesh().cleanMesh();
        skyBox.getShaderProgram().cleanup();
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }
    
    public SceneLight getSceneLight() {
    	return this.sceneLight;
    }
    
    public PlayerModel getPlayerModel() {
    	return this.playerModel;
    }
    
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Map<Mesh, List<GameItem>> getOpaqueMeshMap() {
        return Collections.unmodifiableMap(opaqueMeshMap);
    }

    public Map<Mesh, List<GameItem>> getTransparentMeshMap() {
        return Collections.unmodifiableMap(transparentMeshMap);
    }

    public Player getPlayer() {
        return player;
    }

	
}
