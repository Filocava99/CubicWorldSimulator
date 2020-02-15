package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Mesh;

import java.util.*;

public class Scene {

    private Map<Mesh, List<GameItem>> meshMap = new HashMap<>();
    private final ShaderProgram shaderProgram;

    public Scene(Map<Mesh, List<GameItem>> meshMap, ShaderProgram shaderProgram) {
        this.meshMap = meshMap;
        this.shaderProgram = shaderProgram;
    }

    public Scene(ShaderProgram shaderProgram, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
    }

    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
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

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    //TODO Unmodifiable map? Not sure :(
    public Map<Mesh, List<GameItem>> getMeshMap() {
        return Collections.unmodifiableMap(meshMap);
    }

}
