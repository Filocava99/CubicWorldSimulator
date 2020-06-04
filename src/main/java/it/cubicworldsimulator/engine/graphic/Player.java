package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.game.utility.Constants;

import java.util.HashSet;
import java.util.Set;

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
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
       
        position.y += offsetY;
   
        this.observer.stream().forEach( (e)->{
        	e.update(this.position, this.rotation);
        });
        
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
}
