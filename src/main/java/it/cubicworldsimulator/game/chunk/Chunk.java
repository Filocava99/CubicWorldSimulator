package it.cubicworldsimulator.game.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector3f;

public class Chunk {
    private final byte[] blocks;
    private final Vector3f position;
    private final boolean wasModified;

    public Chunk(byte[] blocks, Vector3f position) {
        if(blocks.length != Constants.chunkTotalBlocks){
            throw new IllegalArgumentException("Blocks array must be of exactly " + Constants.chunkTotalBlocks + " elements!");
        }
        this.wasModified = false;
        this.blocks = blocks;
        this.position = position;
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public Vector3f getPosition() {
        return position;
    }

    public byte getBlock(int x, int y, int z) {
        return blocks[x + (y << 8) + (z << 4)];
    }

    public byte getBlock(Vector3f coord) {
        return blocks[(int) coord.x + (int) (coord.y) << 8 + (int) (coord.z) << 4];
    }

    public boolean wasModified() {
        return wasModified;
    }
}