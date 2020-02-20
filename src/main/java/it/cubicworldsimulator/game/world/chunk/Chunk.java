package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.utility.math.SerializableVector3f;
import org.joml.Vector3f;

import java.io.Serializable;

public class Chunk implements Serializable {
    private final byte[] blocks;
    private final SerializableVector3f position;
    private boolean wasModified;

    public Chunk(byte[] blocks, SerializableVector3f position) {
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

    public void setWasModified(boolean flag){
        this.wasModified = flag;
    }
}