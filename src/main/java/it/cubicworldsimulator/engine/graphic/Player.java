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
    	Vector3i chunkCoord = this.playerCoordToChunkCoord(this.position);
        final boolean result = !chunkCoord.equals(this.lastChunk);
        if(result){
            this.lastChunk = chunkCoord;
        }
        return result;
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
    
    public boolean canPlayerMove(Vector3f offsetPlayer, WorldManager worldManager) {
    	
  		Vector3f newPlayerPosition = this.calculateNewPosition(offsetPlayer);
      	Vector3i chunkCoord = this.playerCoordToChunkCoord(newPlayerPosition);
      	
      	ChunkColumn chunkColumn = null;
      	try {
      		chunkColumn = worldManager.getWorld().getActiveChunks().get(new Vector2f(chunkCoord.x,chunkCoord.z));
      	} catch(NullPointerException e) {
      		return false;
      	}
      	Vector3i blockCoord = this.playerCoordToBlockCoord(new Vector3i((int)newPlayerPosition.x , (int)newPlayerPosition.y, (int)newPlayerPosition.z ));
      	byte blockId = chunkColumn.getBlock(blockCoord);
      	String newPlayerPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
  		
  		return newPlayerPositionMaterial.contentEquals("air");
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
         
         return newPosition;
    }
    
    public Vector3i playerCoordToChunkCoord(Vector3f playerCoord) {
    	return new Vector3i((int) Math.floor(playerCoord.x / Constants.chunkAxisSize), 
    			(int) Math.floor(playerCoord.y / Constants.chunkAxisSize), 
    			(int) Math.floor(playerCoord.z / Constants.chunkAxisSize));
    }

    public Vector3i playerCoordToBlockCoord(Vector3i playerCoord) {
    	Vector3i blockCoord = new Vector3i(0, Math.abs(playerCoord.y),0);

    	if (playerCoord.x >= 0) {
    		blockCoord.x = playerCoord.x % 16;
    	}
    	else {
    		blockCoord.x = (Constants.chunkAxisSize - 1) - (Math.abs(playerCoord.x) % 16);
    	}
    	
    	if (playerCoord.z >= 0) {
    		blockCoord.z = playerCoord.z % 16;
    	}
    	else {
    		blockCoord.z = (Constants.chunkAxisSize - 1) - (Math.abs(playerCoord.z) % 16);
    	}
    	
    	return blockCoord;
    }

}
