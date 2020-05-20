package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector3f;
import org.joml.Vector3i;
public class Player implements Observer{
	private static final float DISTANCE_FROM_CAMERA = 30;
    private Vector3f lastPos;
    private Vector3f position;
    private Vector3i lastChunk;
    private GameItem playerModel;
    private View view;
    
    public Player(String objModel, String textureFile, Vector3f position){
    	this.position  = position;
    	this.view = View.FIRSTPERSON;
    	OBJLoader objLoader = new OBJLoader();
		try {
			Mesh playerMesh = objLoader.loadFromOBJ(objModel, textureFile);
			this.playerModel = new GameItem(playerMesh);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void changeView(View view) {
    	this.view = view;
    }
  
    public boolean didPlayerMove(){
        final boolean result = this.position.equals(lastPos);
        if(result){
            lastPos = this.position;
        }
        return result;
    }

    public boolean didPlayerChangedChunk(){
    	Vector3i chunkCoord = worldCoordToChunkCoord(this.position);
        final boolean result = !chunkCoord.equals(lastChunk);
        if(result){
            lastChunk = chunkCoord;
        }
        return result;
    }

    private Vector3i worldCoordToChunkCoord(Vector3f position) {
    	return new Vector3i((int) Math.floor(position.x / Constants.chunkAxisSize), 
    			(int) Math.floor(position.y / Constants.chunkAxisSize), 
    			(int) Math.floor(position.z / Constants.chunkAxisSize));
    }

    public Vector3i getChunkPosition() {
        return lastChunk;
    }
    
    public GameItem getPlayerModel() {
    	return this.playerModel;
    }

	@Override
	public void update(Vector3f position) {
		this.position = new Vector3f(position.x, position.y, position.z);
		if(this.view.equals(View.THIRDPERSON)) {
			//CAVA E' QUESTO CHE VA CORRETTO 
			this.position.x += DISTANCE_FROM_CAMERA;
			this.position.y -= DISTANCE_FROM_CAMERA;
			this.position.z += DISTANCE_FROM_CAMERA;
		}
		System.out.println("PLAYER POSITION:" + this.position.x + " " + this.position.y + " " + this.position.z);
	}
}
