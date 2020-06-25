package it.cubicworldsimulator.game.world.chunk;

public class ChunkGenerator {

    private final GenerationAlgorithm generationAlgorithm;

    /**
     * @param generationAlgorithm generation algorithm to be used during the chunk generation
     */
    public ChunkGenerator(GenerationAlgorithm generationAlgorithm) {
        this.generationAlgorithm = generationAlgorithm;
    }

    /**
     * Generates a chunk column of X,Z coordinates
     * @param chunkX chunk column X coordinate
     * @param chunkZ chunk column Z coordinate
     * @return generated chunk column
     */
    public ChunkColumn generateChunkColumn(int chunkX, int chunkZ) {
        return generationAlgorithm.generateChunkColumn(chunkX, chunkZ);
    }
}
