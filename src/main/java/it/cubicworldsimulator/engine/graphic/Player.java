package it.cubicworldsimulator.engine.graphic;


import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player extends Camera{

    private Vector3f lastPos;
    private Vector3i lastChunk;
	private ChunkColumn chunkColumn;

    public Player(){

    }

    //VERONIKA //mi sembra non serva
    public boolean didPlayerMove(){
        final boolean result = getPosition().equals(lastPos);
        if(result){
            lastPos = getPosition();
        }
        return result;
    }

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
    
    //VERONIKA
    public boolean canPlayerMove(byte airId) {
    	if(this.lastChunk == null) return false;
    	int heightChunk = chunkColumn.getHeight(new Vector2i(lastChunk.x,lastChunk.y), airId);
    	if(getPosition().y <= heightChunk) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
}
