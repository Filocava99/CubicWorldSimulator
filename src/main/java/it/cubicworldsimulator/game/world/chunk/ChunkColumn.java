package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector2f;
import org.joml.Vector3i;

import java.io.Serializable;

public class ChunkColumn implements Serializable {
    private final Chunk[] chunks;
    private final Vector2f position;

    public ChunkColumn(Chunk[] chunks, Vector2f position){
        if(chunks.length != Constants.chunksPerColumn){
            throw new IllegalArgumentException("Chunks array must be of exactly " + Constants.chunksPerColumn + " elements!");
        }
        this.chunks = chunks;
        this.position = position;
    }

    //TODO Togliere magic numers
    public void setBlock(Vector3i coord, byte blockId){
        int chunkY = coord.y/16;
        Chunk chunk = chunks[coord.y/16]; //sarebbe diviso 16
        int blockY = coord.y-chunkY*16;
        chunk.setBlock(new Vector3i(coord.x, blockY, coord.z), blockId);
    }

    public byte getBlock(Vector3i coord){
        int chunkY = coord.y/16;
        Chunk chunk = chunks[coord.y/16]; //sarebbe diviso 16
        int blockY = coord.y-chunkY*16;
        return chunk.getBlock(coord.x, blockY, coord.z); //TODO Vector3f vs Vector3i
    }

    public Chunk[] getChunks() {
        return chunks;
    }

    public Vector2f getPosition() {
        return position;
    }
}
