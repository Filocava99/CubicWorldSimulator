package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.utility.math.SerializableVector3f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.Serializable;
public class Chunk implements Serializable {
	private static final long serialVersionUID = 1L;
	private final byte[] blocks;
    private final SerializableVector3f position;
    private boolean wasModified;

    /**
     * @param blocks blocks of the chunk
     * @param position position of the chunk
     */
    public Chunk(byte[] blocks, SerializableVector3f position) {
        if(blocks.length != Constants.chunkTotalBlocks){
            throw new IllegalArgumentException("Blocks array must be of exactly " + Constants.chunkTotalBlocks + " elements!");
        }
        this.wasModified = false;
        this.blocks = blocks;
        this.position = position;
    }

    /**
     * @return the blocks of the chunk
     */
    public byte[] getBlocks() {
        return blocks;
    }

    /**
     * @return the chunk position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Change a block of the chunk
     * @param x
     * @param y
     * @param z
     * @param blockId block id
     */
    public void setBlock(int x, int y, int z, byte blockId){
        blocks[x + (y << 8) + (z << 4)] = blockId;
    }

    /**
     * Change a block of the chunk
     * @param coord block coordinates
     * @param blockId block id
     */
    public void setBlock(Vector3i coord, byte blockId){
        setBlock(coord.x, coord.y, coord.z, blockId);
    }

    /**
     * Returns the block id at a given positon
     * @param x
     * @param y
     * @param z
     * @return blocks id
     */
    public byte getBlock(int x, int y, int z) {
        return blocks[x + (y * Constants.chunkAxisSize * Constants.chunkAxisSize) + (z * Constants.chunkAxisSize)];
    }

    /**
     * Returns the block id at a given positon
     * @param coord block position
     * @return blocks id
     */
    public byte getBlock(Vector3i coord) {
        return blocks[coord.x +  coord.y * Constants.chunkAxisSize * Constants.chunkAxisSize + (int) (coord.z) * Constants.chunkAxisSize];
    }

    /**
     * @return true if the chunk was recently modified
     */
    public boolean wasModified() {
        return wasModified;
    }

    /**
     * Set the wasModified flag
     * @param flag boolean
     */
    public void setWasModified(boolean flag){
        this.wasModified = flag;
    }

}