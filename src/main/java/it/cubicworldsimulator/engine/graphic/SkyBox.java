package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;

public class SkyBox extends GameItem {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        OBJLoader objLoader = new OBJLoader();
        Mesh skyBoxMesh = objLoader.loadFromOBJ(objModel, textureFile);
        setPosition(0, 0, 0);
    }
}
