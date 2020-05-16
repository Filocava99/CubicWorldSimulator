package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import it.cubicworldsimulator.game.world.WorldManager;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;

public class Player {

    private Vector3f lastPos;
    private Vector3i lastChunk;
    private final Camera camera;
    private ChunkColumn chunkColumn;
    
    public Player(Camera camera){
        this.camera = camera;
    }

    //VERONIKA //mi sembra non serva
    public boolean didPlayerMove(){
        final boolean result = camera.getPosition().equals(lastPos);
        if(result){
            lastPos = camera.getPosition();
        }
        return result;
    }

    public boolean didPlayerChangedChunk(){
        Vector3i chunkCoord = worldCoordToChunkCoord(camera.getPosition());
        final boolean result = !chunkCoord.equals(lastChunk);
        if(result){
            lastChunk = chunkCoord;
        }
        return result;
    }

    private Vector3i worldCoordToChunkCoord(Vector3f position) {
        return new Vector3i((int) Math.floor(position.x / 16), (int) Math.floor(position.y / 16), (int) Math.floor(position.z / 16)); //TODO Ottimizzare con gli shift <<
    }

    public Vector3i getChunkPosition() {
        return lastChunk;
    }
    
    //VERONIKA
    public boolean canPlayerMove(byte airId) {
    	if(this.lastChunk == null) return false;
    	int heightChunk = chunkColumn.getHeight(new Vector2i(lastChunk.x,lastChunk.y), airId);
    	if(camera.getPosition().y <= heightChunk) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
}
