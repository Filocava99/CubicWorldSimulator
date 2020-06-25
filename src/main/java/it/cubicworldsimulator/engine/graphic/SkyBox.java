package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.ShaderProgram;
import it.cubicworldsimulator.engine.loader.OBJLoader;

public class SkyBox extends GameItem {

    private final ShaderProgram shaderProgram;

    /**
     * @param objModel obj file path
     * @param textureFile texture file path
     * @param shaderProgram skybox shader program
     */
    public SkyBox(String objModel, String textureFile, ShaderProgram shaderProgram) {
        super();
        OBJLoader objLoader = new OBJLoader();
        Mesh skyBoxMesh = objLoader.loadFromOBJ(objModel, textureFile);
        this.setMesh(skyBoxMesh);
        this.setPosition(0,0,0);
        this.setScale(50);
        this.shaderProgram = shaderProgram;
    }

    /**
     * Returns the shader program
     * @return the skybox shader program
     */
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}