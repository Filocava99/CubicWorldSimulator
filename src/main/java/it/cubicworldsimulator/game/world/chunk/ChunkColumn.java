package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.io.Serializable;

public class ChunkColumn implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Chunk[] chunks;
    private final Vector2f position;

    /**
     * @param chunks array of the chunks of the chunk column
     * @param position coordinates of the chunk column
     */
    public ChunkColumn(Chunk[] chunks, Vector2f position){
        if(chunks.length != Constants.chunksPerColumn){
            throw new IllegalArgumentException("Chunks array must be of exactly " + Constants.chunksPerColumn + " elements!");
        }
        this.chunks = chunks;
        this.position = position;
    }

    /**
     * Change the block at the specified coordinates
     * @param coord block coords
     * @param blockId block id
     */
    public void setBlock(Vector3i coord, byte blockId){
        int chunkY = coord.y/Constants.chunkAxisSize;
        Chunk chunk = chunks[coord.y/Constants.chunkAxisSize];
        int blockY = coord.y-chunkY*Constants.chunkAxisSize;
        chunk.setBlock(new Vector3i(coord.x, blockY, coord.z), blockId);
    }

    /**
     * Returns the height at a specified set of coordinates
     * @param coord coordinates
     * @param airId id of the air block
     * @return the Y value (intenger) of the first air block found
     */
    public int getHeight(Vector2i coord, byte airId) {
        for (int i = Constants.minHeight; i < Constants.maxHeight-1; i++) {
            byte blockId = getBlock(new Vector3i(coord.x, i, coord.y));
            if (blockId == airId) {
                return Math.max(0, i);
            }
        }
        return Constants.maxHeight-1; //Dovrebbe essere 255 di default
    }

    /**
     * Returns the block id at specified coordinates
     * @param coord coordinates
     * @return the block id (byte)
     */
    public byte getBlock(Vector3i coord){
        int chunkY = (int) Math.floor(coord.y/Constants.chunkAxisSize);
        Chunk chunk = chunks[chunkY];
        int blockY = coord.y-chunkY*Constants.chunkAxisSize;
        return chunk.getBlock(coord.x, blockY, coord.z);
    }

    /**
     * @return The array of chunks that makes the chunk column
     */
    public Chunk[] getChunks() {
        return chunks;
    }

    /**
     * @return the coordinates of the chunk column
     */
    public Vector2f getPosition() {
        return position;
    }
}
