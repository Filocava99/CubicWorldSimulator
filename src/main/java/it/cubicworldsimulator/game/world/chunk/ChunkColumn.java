package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.joml.Vector2f;

public class ChunkColumn {
    private final Chunk[] chunks;
    private final Vector2f position;

    public ChunkColumn(Chunk[] chunks, Vector2f position){
        if(chunks.length != Constants.chunksPerColumn){
            throw new IllegalArgumentException("Chunks array must be of exactly " + Constants.chunksPerColumn + " elements!");
        }
        this.chunks = chunks;
        this.position = position;
    }
}
