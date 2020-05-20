package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector3f;
import org.joml.Vector3i;
public class Player{
	private static final float DISTANCE_FROM_CAMERA = 50;
    private Vector3f lastPos;
    private Vector3f position;
    private Vector3i lastChunk;
    private GameItem playerModel;
    private Camera camera;
    private View view;
    
    public Player(String objModel, String textureFile){
    	this.position  = new Vector3f(0, 35, 0);
    	this.view = View.FIRSTPERSON;
    	this.camera = new Camera(new Vector3f(this.position.x,this.position.y,this.position.z));
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
    
    public void movePlayer() {
    	this.position.x = this.camera.getPosition().x;
    	this.position.y = this.camera.getPosition().y;
    	this.position.z = this.camera.getPosition().z;
    	
    	if(this.view.equals(View.THIRDPERSON)) {
    		/*this.position.x += (float)Math.sin(Math.toRadians(this.camera.getRotation().y)) * -1.0f * DISTANCE_FROM_CAMERA;
    		this.position.x += (float)Math.sin(Math.toRadians(this.camera.getRotation().y - 90)) * -1.0f * DISTANCE_FROM_CAMERA;
    		
    		this.position.y -= DISTANCE_FROM_CAMERA;
    		
    		this.position.z += (float)Math.sin(Math.toRadians(this.camera.getRotation().y)) * -1.0f * DISTANCE_FROM_CAMERA;
    		this.position.z += (float)Math.cos(Math.toRadians(this.camera.getRotation().y - 90)) * DISTANCE_FROM_CAMERA;*/
    		
    		this.position.x += DISTANCE_FROM_CAMERA;
    		this.position.y -= DISTANCE_FROM_CAMERA;
    		this.position.z += DISTANCE_FROM_CAMERA;
    	}
    	 System.out.println("PLAYER POSITION:" + position.x + " " + position.y + " " + position.z);
    }
    
    public boolean didPlayerMove(){
        final boolean result = this.camera.getPosition().equals(lastPos);
        if(result){
            lastPos = this.camera.getPosition();
        }
        return result;
    }

    public boolean didPlayerChangedChunk(){
        //Vector3i chunkCoord = worldCoordToChunkCoord(this.camera.getPosition());
    	Vector3i chunkCoord = worldCoordToChunkCoord(this.position);
        final boolean result = !chunkCoord.equals(lastChunk);
        if(result){
            lastChunk = chunkCoord;
        }
        return result;
    }

    private Vector3i worldCoordToChunkCoord(Vector3f position) {
        //return new Vector3i((int) Math.floor(this.camera.getPosition().x / Constants.chunkAxisSize), (int) Math.floor(this.camera.getPosition().y / Constants.chunkAxisSize), (int) Math.floor(this.camera.getPosition().z / Constants.chunkAxisSize));
    	return new Vector3i((int) Math.floor(position.x / Constants.chunkAxisSize), (int) Math.floor(position.y / Constants.chunkAxisSize), (int) Math.floor(position.z / Constants.chunkAxisSize));
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
