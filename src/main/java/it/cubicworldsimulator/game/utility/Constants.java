package it.cubicworldsimulator.game.utility;

import java.io.File;

public class Constants {

    public static final int chunkAxisSize = 16;
    public static final int chunkTotalBlocks;
    public static final int chunksPerColumn = 16;
    public static final int maxHeight;
    public static final int minHeight = 0;
    public static final String installationFolder;

    static {
        chunkTotalBlocks = chunkAxisSize * chunkAxisSize * chunkAxisSize;
        maxHeight = chunkAxisSize * chunksPerColumn;
        final String osName = System.getProperty("os.name");
        if(osName.contains("Windows")){
            installationFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "CubicWorldSimulator";
        }else if(osName.contains("Mac")){
            installationFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "CubicWorldSimulator";
        }else{
            installationFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "CubicWorldSimulator";
        }
        System.out.println(installationFolder);
    }

}
