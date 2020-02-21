package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector2f;

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

    public Chunk[] getChunks() {
        return chunks;
    }

    public Vector2f getPosition() {
        return position;
    }
}
