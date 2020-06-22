package it.cubicworldsimulator.game.world.chunk;

public class ChunkGenerator {

    private final GenerationAlgorithm generationAlgorithm;

    public ChunkGenerator(GenerationAlgorithm generationAlgorithm) {
        this.generationAlgorithm = generationAlgorithm;
    }

    public ChunkColumn generateChunkColumn(int chunkX, int chunkZ) {
        return generationAlgorithm.generateChunkColumn(chunkX, chunkZ);
    }
}
