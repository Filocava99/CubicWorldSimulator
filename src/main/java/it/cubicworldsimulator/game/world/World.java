package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.game.world.chunk.Chunk;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;
import org.joml.Vector2f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final List<Chunk> chunks;
    private final long seed;
    private final String name;
    private final ConcurrentHashMap<Vector2f, ChunkColumn> activeChunks = new ConcurrentHashMap<>(); //TODO Non mi piace

    public World(String name, long seed){
        chunks = new LinkedList<>();
        this.name = name;
        this.seed = seed;
    }

    public List<Chunk> getChunks() {
        return Collections.unmodifiableList(chunks);
    }

    public long getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    protected Map<Vector2f, ChunkColumn> getActiveChunks() {
        return activeChunks;
    }
}
