package it.cubicworldsimulator.engine.graphic;


import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.WorldManager;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player implements Observable{
	
    private Vector3f lastPosition;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3i lastChunk;
	private final Set<Observer> observer= new HashSet<>();

    public Player(Vector3f position){
    	this.position  = position;
    	this.rotation = new Vector3f(0,0,0);
    }
    
    public Vector3f getPosition() {
    	return this.position;
    }
    
    public Vector3f getRotation() {
    	return this.rotation;
    }
    
    public Vector3i getChunkPosition() {
        return this.lastChunk;
    }
    
    public void attach(Observer observer) {
    	this.observer.add(observer);
    }

    public boolean didPlayerMove(){
        final boolean result = this.position.equals(this.lastPosition);
        if(result){
            this.lastPosition = this.position;
        }
        return result;
    }

    public boolean didPlayerChangedChunk(){
    	Vector3i chunkCoord = worldCoordToChunkCoord(this.position);
        final boolean result = !chunkCoord.equals(this.lastChunk);
        if(result){
            this.lastChunk = chunkCoord;
        }
        return result;
    }
    
    private Vector3i worldCoordToChunkCoord(Vector3f position) {
    	return new Vector3i((int) Math.floor(position.x / Constants.chunkAxisSize), 
    			(int) Math.floor(position.y / Constants.chunkAxisSize), 
    			(int) Math.floor(position.z / Constants.chunkAxisSize));
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
    	this.position = calculateNewPosition(offsetX, offsetY, offsetZ);
   
        this.observer.stream().forEach( (e)->{
        	e.update(this.position, this.rotation);
        });   
    }
    
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        this.rotation.x += offsetX;
        this.rotation.y += offsetY;
        this.rotation.z += offsetZ;
        
        this.observer.stream().forEach( (e)->{
        	e.update(this.position, this.rotation);
        });
    }
    
    private Vector3f calculateNewPosition(float offsetX, float offsetY, float offsetZ) {
    	Vector3f newPosition = new Vector3f(this.position);
    	
    	 if ( offsetZ != 0 ) {
             newPosition.x += (float)Math.sin(Math.toRadians(this.rotation.y)) * -1.0f * offsetZ;
             newPosition.z += (float)Math.cos(Math.toRadians(this.rotation.y)) * offsetZ;
         }
         if ( offsetX != 0) {
        	 newPosition.x += (float)Math.sin(Math.toRadians(this.rotation.y - 90)) * -1.0f * offsetX;
        	 newPosition.z += (float)Math.cos(Math.toRadians(this.rotation.y - 90)) * offsetX;
         }
        
         newPosition.y += offsetY;
         
         System.out.println("Old Position: " + this.position + " NewPosition: " + newPosition);
         return newPosition;
    }
    
    public boolean canPlayerMove(Vector3f offsetCamera, WorldManager worldManager) {
    	System.out.println("offsetCamera: " + offsetCamera);
    	
  		Vector3f newCameraPosition = this.calculateNewPosition(offsetCamera.x, offsetCamera.y, offsetCamera.z);
  		
      	Vector3i newChunkCoord = worldCoordToChunkCoord(newCameraPosition);
      	System.out.println("newChunkCoord: " + newChunkCoord);
      	
      	ChunkColumn chunkColumn = worldManager.getWorld().getActiveChunks().get(new Vector2f(newChunkCoord.x,newChunkCoord.z));
      	System.out.println("chunkColumn: " + chunkColumn.getPosition());
      	
      	if(chunkColumn == null) {
      		return false;
      	}
      	
      	Vector3i blockCoord = chunkColumn.worldCoordToBlockCoord(new Vector3i((int)newCameraPosition.x , (int)newCameraPosition.y, (int)newCameraPosition.z ));
      	 
      	byte blockId = chunkColumn.getBlock(blockCoord);
      	System.out.println("blockId: " + blockId);
      	
      	String newCameraPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
  		System.out.println("newCameraPositionMaterial: " + newCameraPositionMaterial + "\n");
  		
  		return newCameraPositionMaterial.contentEquals("air");
      }

}
