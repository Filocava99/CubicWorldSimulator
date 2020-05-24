package it.cubicworldsimulator.engine.graphic;


import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.World;
import it.cubicworldsimulator.game.world.WorldManager;
import it.cubicworldsimulator.game.world.block.BlockMaterial;
import it.cubicworldsimulator.game.world.chunk.Chunk;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player extends Camera{

   // private Vector3f lastPos;
    private Vector3i lastChunk;

    public Player(){
    	super();
    	this.lastChunk = worldCoordToChunkCoord(getPosition());
    }

	/*
	 * public boolean didPlayerMove(){ final boolean result =
	 * getPosition().equals(lastPos); if(result){ lastPos = getPosition(); } return
	 * result; }
	 */

    public boolean didPlayerChangedChunk(){
        Vector3i chunkCoord = worldCoordToChunkCoord(getPosition());
        final boolean result = !chunkCoord.equals(lastChunk);
        if(result){
            lastChunk = chunkCoord;
        }
        return result;
    }

    private Vector3i worldCoordToChunkCoord(Vector3f position) {
        return new Vector3i((int) Math.floor(position.x / Constants.chunkAxisSize), (int) Math.floor(position.y / Constants.chunkAxisSize), (int) Math.floor(position.z / Constants.chunkAxisSize));
    }

    public Vector3i getChunkPosition() {
        return lastChunk;
    }
    
	
	/*public boolean canPlayerMove(byte airId, Vector3f newCameraPosition,ChunkColumn chunkColumn) { 
		Vector3i newChunkCoord = worldCoordToChunkCoord(newCameraPosition); 
		int heightCamera = chunkColumn.getHeight(new Vector2i(Math.abs(newChunkCoord.x),Math.abs(newChunkCoord.z)), airId); int
		heightChunk2 = chunkColumn.getHeight(new Vector2i(Math.abs(lastChunk.x),Math.abs(lastChunk.z)), airId); 
		return heightChunk2 ==  heightCamera; 
	 }*/
	 
    
    /*public boolean canPlayerMove(Vector3f offsetCamera, World world, WorldManager worldManager) {
    		Vector3f newCameraPosition = this.calculateNewPosition(offsetCamera);
        	Vector3i newChunkCoord = worldCoordToChunkCoord(newCameraPosition);
        	ChunkColumn chunkColumn = world.getActiveChunks().get(new Vector2f(Math.abs(newChunkCoord.x),Math.abs(newChunkCoord.z)));
        	byte blockId = chunkColumn.getBlock(new Vector3i(Math.abs(newChunkCoord.x), Math.abs(newChunkCoord.y), Math.abs(newChunkCoord.z)));
    		String newCameraPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
    		
    		System.out.println("newCameraPositionMaterial: " + newCameraPositionMaterial);
    		
    		return newCameraPositionMaterial.contentEquals("air");
    }*/
    
    public boolean canPlayerMove(Vector3f offsetCamera, World world, WorldManager worldManager) {
		Vector3f newCameraPosition = this.calculateNewPosition(offsetCamera);
    	Vector3i newChunkCoord = worldCoordToChunkCoord(newCameraPosition);
    	System.out.println("newChunkCoord: " + newChunkCoord);
    	
    	ChunkColumn chunkColumn = world.getActiveChunks().get(new Vector2f(newChunkCoord.x,newChunkCoord.z));
    	System.out.println("chunkColumn: " + chunkColumn.getPosition());
    	
    	byte blockId = chunkColumn.getBlock(new Vector3i((int)Math.floor(Math.abs(newCameraPosition.x)),(int)Math.floor(Math.abs(newCameraPosition.y)), (int)Math.floor(Math.abs(newCameraPosition.z))));
    	System.out.println("blockId: " + blockId);
    	
    	String newCameraPositionMaterial = worldManager.getBlockTypes().get(blockId).getName();
		
		System.out.println("newCameraPositionMaterial: " + newCameraPositionMaterial);
		
		return newCameraPositionMaterial.contentEquals("air");
    }
}
