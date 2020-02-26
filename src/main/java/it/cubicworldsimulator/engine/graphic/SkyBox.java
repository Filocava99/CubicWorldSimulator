package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.ShaderProgram;

public class SkyBox extends GameItem {

    private final ShaderProgram shaderProgram;

    public SkyBox(String objModel, String textureFile, ShaderProgram shaderProgram) throws Exception {
        super();
        OBJLoader objLoader = new OBJLoader();
        Mesh skyBoxMesh = objLoader.loadFromOBJ(objModel, textureFile);
        this.setMesh(skyBoxMesh);
        this.setPosition(0,0,0);
        this.setScale(200);
        this.shaderProgram = shaderProgram;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}