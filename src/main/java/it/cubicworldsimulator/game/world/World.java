package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.game.world.chunk.Chunk;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;
import org.joml.Vector2f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final List<Chunk> chunks; //Serve per future estensioni del gioco, al momento non ha un utilizzo
    private final long seed;
    private final String name;
    private final ConcurrentHashMap<Vector2f, ChunkColumn> activeChunks = new ConcurrentHashMap<>();

    /**
     * @param name world name
     * @param seed world seed
     */
    public World(String name, long seed){
        chunks = new LinkedList<>();
        this.name = name;
        this.seed = seed;
    }

    /**
     * Returns the list of the chunk of the world
     * @return unmodifiable list of the chunk of the world
     */
    public List<Chunk> getChunks() {
        return Collections.unmodifiableList(chunks);
    }

    /**
     * @return the seed of the world
     */
    public long getSeed() {
        return seed;
    }

    /**
     * @return the name of the world
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the map of the currently loaded chunks
     * @return map of the currently loaded chunks
     */
    public Map<Vector2f, ChunkColumn> getActiveChunks() {
        return activeChunks;
    }
}
