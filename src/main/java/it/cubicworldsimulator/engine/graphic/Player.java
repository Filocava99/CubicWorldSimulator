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
    	Vector3i chunkCoord = PlayerCoordToChunkCoord(this.position);
        final boolean result = !chunkCoord.equals(this.lastChunk);
        if(result){
            this.lastChunk = chunkCoord;
        }
        return result;
    }
    
    private Vector3i PlayerCoordToChunkCoord(Vector3f position) {
    	return new Vector3i((int) Math.floor(position.x / Constants.chunkAxisSize), 
    			(int) Math.floor(position.y / Constants.chunkAxisSize), 
    			(int) Math.floor(position.z / Constants.chunkAxisSize));
    }

    private Vector3i PlayerCoordToBlockCoord(Vector3i worldCoord) {
    	Vector3i blockCoord = new Vector3i(0, Math.abs(worldCoord.y),0);
    	
    	if (worldCoord.x >= 0) {
    		blockCoord.x = worldCoord.x % 16;
    	}
    	else {
    		blockCoord.x = (Constants.chunkAxisSize - 1) - (Math.abs(worldCoord.x) % 16);
    	}
    	
    	if (worldCoord.z >= 0) {
    		blockCoord.z = worldCoord.z % 16;
    	}
    	else {
    		blockCoord.z = (Constants.chunkAxisSize - 1) - (Math.abs(worldCoord.z) % 16);
    	}
    	
    	System.out.println("blockCoord: " + blockCoord);
    	return blockCoord;
    }
    
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
    	this.position = calculateNewPosition(new Vector3f(offsetX, offsetY, offsetZ));
   
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
    
    private Vector3f calculateNewPosition(Vector3f offset) {
    	Vector3f newPosition = new Vector3f(this.position);
    	
    	 if ( offset.z != 0 ) {
             newPosition.x += (float)Math.sin(Math.toRadians(this.rotation.y)) * -1.0f * offset.z;
             newPosition.z += (float)Math.cos(Math.toRadians(this.rotation.y)) * offset.z;
         }
         if ( offset.x != 0) {
        	 newPosition.x += (float)Math.sin(Math.toRadians(this.rotation.y - 90)) * -1.0f * offset.x;
        	 newPosition.z += (float)Math.cos(Math.toRadians(this.rotation.y - 90)) * offset.x;
         }
        
         newPosition.y += offset.y;
         
         System.out.println("Old Position: " + this.position + " NewPosition: " + newPosition);
         return newPosition;
    }
    
    public boolean canPlayerMove(Vector3f offsetPlayer, WorldManager worldManager) {
    	System.out.println("offsetCamera: " + offsetPlayer);
    	
  		Vector3f newPlayerPosition = this.calculateNewPosition(offsetPlayer);
  		
      	Vector3i chunkCoord = PlayerCoordToChunkCoord(newPlayerPosition);
      	System.out.println("newChunkCoord: " + chunkCoord);
      	
      	ChunkColumn chunkColumn = worldManager.getWorld().getActiveChunks().get(new Vector2f(chunkCoord.x,chunkCoord.z));
      	System.out.println("chunkColumn: " + chunkColumn.getPosition());
      	
        if(chunkColumn == null) {
      		return false;
      	}
      	
      	Vector3i blockCoord = this.PlayerCoordToBlockCoord(new Vector3i((int)newPlayerPosition.x , (int)newPlayerPosition.y, (int)newPlayerPosition.z ));
      	 
      	byte blockId = chunkColumn.getBlock(blockCoord);
      	System.out.println("blockId: " + blockId);
      	
      	String newPlayerPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
  		System.out.println("newCameraPositionMaterial: " + newPlayerPositionMaterial + "\n");
  		
  		return newPlayerPositionMaterial.contentEquals("air");
      }

}
