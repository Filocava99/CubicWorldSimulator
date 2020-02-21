package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.SkyBox;

import java.util.*;

public class Scene {

    private Map<Mesh, List<GameItem>> meshMap = new HashMap<>();
    private final ShaderProgram shaderProgram;
    private final SkyBox skyBox;

    public Scene(Map<Mesh, List<GameItem>> meshMap, ShaderProgram shaderProgram, SkyBox skyBox) {
        this.meshMap = meshMap;
        this.shaderProgram = shaderProgram;
        this.skyBox = skyBox;
    }

    public Scene(ShaderProgram shaderProgram, SkyBox skyBox, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
    }

    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram, SkyBox skyBox){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
    }

    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public void cleanUp(){
        meshMap.keySet().forEach(Mesh::cleanUp);
        shaderProgram.cleanup();
        skyBox.getMesh().cleanUp();
        skyBox.getShaderProgram().cleanup();
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    //TODO Unmodifiable map? Not sure :(
    public Map<Mesh, List<GameItem>> getMeshMap() {
        return Collections.unmodifiableMap(meshMap);
    }

}
