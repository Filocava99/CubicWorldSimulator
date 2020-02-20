package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.game.world.chunk.Chunk;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class World {

    private final List<Chunk> chunks;
    private final long seed;
    private final String name; //Non si puo' cambiare il nome del mondo a runtime. Oppure vogliamo lasciarlo fare? Boh

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
