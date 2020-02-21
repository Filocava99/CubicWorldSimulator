package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.game.world.chunk.Chunk;
import it.cubicworldsimulator.game.world.chunk.ChunkColumn;
import org.joml.Vector2f;

import java.util.*;

public class World {

    private final List<Chunk> chunks;
    private final long seed;
    private final String name; //Non si puo' cambiare il nome del mondo a runtime. Oppure vogliamo lasciarlo fare? Boh
    private final Map<Vector2f, ChunkColumn> activeChunks = new HashMap<>();

    public World(String name, long seed){
        chunks = new LinkedList<>(); //TODO Valutare la scelta della linkedList
        this.name = name;
        this.seed = seed;
    }

    public List<Chunk> getChunks() {
        return Collections.unmodifiableList(chunks); //TODO Ha senso una unmodifiableList? Penso di si
    }

    public long getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }
}
