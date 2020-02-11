package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.game.world.block.Material;

import java.util.HashMap;
import java.util.Map;

public class WorldManager {

    private final World world;
    private final Map<Byte, Material> materialMap = new HashMap<>();

    public WorldManager(World world) {
        this.world = world;
    }
}
