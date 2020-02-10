package it.cubicworldsimulator.game.utility;

public class Constants {

    public static final int chunkAxisSize = 16;
    public static final int chunkTotalBlocks;
    public static final int chunksPerColumn = 16;
    public static final int maxHeight;
    public static final int minHeight = 0;

    static {
        chunkTotalBlocks = chunkAxisSize * chunkAxisSize * chunkAxisSize;
        maxHeight = chunkAxisSize * chunksPerColumn;
    }

}
