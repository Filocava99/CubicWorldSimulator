package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.Serializable;

public class ChunkColumn implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Chunk[] chunks;
    private final Vector2f position;

    public ChunkColumn(Chunk[] chunks, Vector2f position){
        if(chunks.length != Constants.chunksPerColumn){
            throw new IllegalArgumentException("Chunks array must be of exactly " + Constants.chunksPerColumn + " elements!");
        }
        this.chunks = chunks;
        this.position = position;
    }
    
    //potrei inserirlo anche nella classe Chunk?
    public Vector3i worldCoordToBlockCoord(Vector3i worldCoord) {
    	Vector3i blockCoord = new Vector3i(0, Math.abs(worldCoord.y),0);
    	
    	if (worldCoord.x >= 0) {
    		blockCoord.x = worldCoord.x % 16;
    	}
    	else {
    		blockCoord.x = Constants.chunkAxisSize - (Math.abs(worldCoord.x) % 16);
    	}
    	
    /*	if (worldCoord.y >= 0) {
    		blockCoord.y = worldCoord.y % 16;
    	}
    	else {
    		blockCoord.y = Constants.chunkAxisSize - (Math.abs(worldCoord.y) % 16);
    	}*/
    	
    	if (worldCoord.z >= 0) {
    		blockCoord.z = worldCoord.z % 16;
    	}
    	else {
    		blockCoord.z = Constants.chunkAxisSize - (Math.abs(worldCoord.z) % 16);
    	}
    	
    	System.out.println("blockCoord: " + blockCoord);
    	return blockCoord;
    }

    public void setBlock(Vector3i coord, byte blockId){
        int chunkY = coord.y/Constants.chunkAxisSize;
        Chunk chunk = chunks[coord.y/Constants.chunkAxisSize];
        int blockY = coord.y-chunkY*Constants.chunkAxisSize;
        chunk.setBlock(new Vector3i(coord.x, blockY, coord.z), blockId);
    }

    public int getHeight(Vector2i coord, byte airId) {
        for (int i = Constants.minHeight; i < Constants.maxHeight-1; i++) {
            byte blockId = getBlock(new Vector3i(coord.x, i, coord.y));
            if (blockId == airId) {
                return Math.max(0, i);
            }
        }
        return Constants.maxHeight-1; //Dovrebbe essere 255 di default
    }


    public byte getBlock(Vector3i coord){
        int chunkY = coord.y/Constants.chunkAxisSize;
        Chunk chunk = chunks[chunkY];
        int blockY = coord.y-chunkY*Constants.chunkAxisSize;
        return chunk.getBlock(coord.x, blockY, coord.z);
    }
    
    public Chunk[] getChunks() {
        return chunks;
    }

    public Vector2f getPosition() {
        return position;
    }
}
