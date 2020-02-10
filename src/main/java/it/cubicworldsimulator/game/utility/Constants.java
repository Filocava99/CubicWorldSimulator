package it.cubicworldsimulator.game.utility;

public class Constants {

    public static final int chunkAxisSize = 16;
    public static final int chunkTotalBlocks;

    static {
        chunkTotalBlocks = chunkAxisSize * chunkAxisSize * chunkAxisSize;
    }

}
