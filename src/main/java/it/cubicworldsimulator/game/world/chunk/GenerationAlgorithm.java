package it.cubicworldsimulator.game.world.chunk;

public interface GenerationAlgorithm {
        /**
         * Generates the chunk column at X,Z coordinates
         * @param x
         * @param z
         * @return the generated chunk column
         */
        ChunkColumn generateChunkColumn(int x, int z);
}
