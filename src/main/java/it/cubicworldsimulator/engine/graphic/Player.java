package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player{

    private Vector3f lastPos;
    private Vector3i lastChunk;
    private GameItem playerModel;
    private Camera camera;
    
    public Player(String objModel, String textureFile){
    	this.camera = new Camera();
    	OBJLoader objLoader = new OBJLoader();
		try {
			Mesh playerMesh = objLoader.loadFromOBJ(objModel, textureFile);
			this.playerModel = new GameItem(playerMesh);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public boolean didPlayerMove(){
        final boolean result = this.camera.getPosition().equals(lastPos);
        if(result){
            lastPos = this.camera.getPosition();
        }
        return result;
    }

    public boolean didPlayerChangedChunk(){
        Vector3i chunkCoord = worldCoordToChunkCoord(this.camera.getPosition());
        final boolean result = !chunkCoord.equals(lastChunk);
        if(result){
            lastChunk = chunkCoord;
        }
        return result;
    }

    private Vector3i worldCoordToChunkCoord(Vector3f position) {
        return new Vector3i((int) Math.floor(this.camera.getPosition().x / Constants.chunkAxisSize), (int) Math.floor(this.camera.getPosition().y / Constants.chunkAxisSize), (int) Math.floor(this.camera.getPosition().z / Constants.chunkAxisSize));
    }

    public Vector3i getChunkPosition() {
        return lastChunk;
    }
    
    public GameItem getPlayerModel() {
    	return this.playerModel;
    }
    
    public Camera getCamera() {
    	return this.camera;
    }
}
