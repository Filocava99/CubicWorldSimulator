package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Player;
import it.cubicworldsimulator.engine.graphic.SkyBox;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.loader.LoaderUtility;

import java.util.*;

public class Scene {

    private Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();
    private final ShaderProgram shaderProgram;
    private final SkyBox skyBox;
    private final SceneLight sceneLight;
    private final Player player = new Player();

    public Scene(Map<Mesh, List<GameItem>> opaqueMeshMap, Map<Mesh, List<GameItem>> transparentMeshMap, ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight) {
        this.opaqueMeshMap = opaqueMeshMap;
        this.transparentMeshMap = transparentMeshMap;
        this.shaderProgram = shaderProgram;
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
    }

    public Scene(ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
    }

    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram, SkyBox skyBox, SceneLight sceneLight){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.sceneLight = sceneLight;
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
        opaqueMeshMap.keySet().forEach(LoaderUtility::cleanMesh);
        transparentMeshMap.keySet().forEach(LoaderUtility::cleanMesh);
        shaderProgram.cleanup();
        LoaderUtility.cleanMesh(skyBox.getMesh());
        skyBox.getShaderProgram().cleanup();
    }

    public SkyBox getSkyBox() {
        return skyBox;
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

    public SceneLight getSceneLight() {
        return sceneLight;
    }
}
