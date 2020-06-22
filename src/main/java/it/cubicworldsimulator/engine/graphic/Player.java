package it.cubicworldsimulator.engine.graphic;


import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;

import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player implements Observable{
	
    private Vector3f lastPos;
    private Vector3f position;
    private Vector3f rotation;

    private Vector3i lastChunk;
    private final Set<Observer> observer= new HashSet<>();


    public Player(Vector3f position){
    	this.position  = position;
    	this.rotation = new Vector3f(0,0,0);
   
    }
    
    public Vector3f getRotation() {
    	return this.rotation;
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

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
    	this.position = calculateNewPosition(offsetX, offsetY, offsetZ);
   
        this.observer.stream().forEach( (e)->{
        	e.update(this.position, this.rotation);
        });
        
    }
    
    private Vector3f calculateNewPosition(float offsetX, float offsetY, float offsetZ) {
    	Vector3f newPosition = new Vector3f(this.position);
    	
    	 if ( offsetZ != 0 ) {
             newPosition.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
             newPosition.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
         }
         if ( offsetX != 0) {
        	 newPosition.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
        	 newPosition.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
         }
        
         newPosition.y += offsetY;
         
         System.out.println("NewPosition: " + newPosition);
         return newPosition;
    }
    
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
        
        this.observer.stream().forEach( (e)->{
        	e.update(this.position, this.rotation);
        });
    }
    
    public Vector3f getPosition() {
    	return this.position;
    }
    
    public void attach(Observer observer) {
    	this.observer.add(observer);
    }
    
    public boolean canPlayerMove(Vector3f offsetCamera, WorldManager worldManager) {
    	System.out.println("offsetCamera: " + offsetCamera);
    	
  		Vector3f newCameraPosition = this.calculateNewPosition(offsetCamera.x, offsetCamera.y, offsetCamera.z);
  		System.out.println("newCameraPosition: " + newCameraPosition);
  		
      	Vector3i newChunkCoord = worldCoordToChunkCoord(newCameraPosition);
      	System.out.println("newChunkCoord: " + newChunkCoord);
      	
      	ChunkColumn chunkColumn = worldManager.getWorld().getActiveChunks().get(new Vector2f(newChunkCoord.x,newChunkCoord.z));
      	System.out.println("chunkColumn: " + chunkColumn.getPosition());
      	
      	byte blockId = chunkColumn.getBlock(new Vector3i((int)Math.floor((newCameraPosition.x)),(int)Math.floor(Math.abs(newCameraPosition.y)), (int)Math.floor((newCameraPosition.z))));
      	System.out.println("blockId: " + blockId);
      	
      	String newCameraPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
  		System.out.println("newCameraPositionMaterial: " + newCameraPositionMaterial + "\n");
  		
  		return newCameraPositionMaterial.contentEquals("air");
      }
}
