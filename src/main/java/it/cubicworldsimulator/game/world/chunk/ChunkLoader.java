package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Optional;

public class ChunkLoader {

    private static final Logger logger = LogManager.getLogger(ChunkLoader.class);
    private final String chunkFolderPath = Constants.installationFolder + File.pathSeparator + "data" + File.pathSeparator + "chunks";

    public Optional<ChunkMesh> loadChunkMesh(Vector2f position) {
        try {
            File chunkFolder = new File(chunkFolderPath);
            chunkFolder.mkdirs();
            File chunkFile = new File(chunkFolderPath + File.pathSeparator + position.x + "_" + position.y + ".chunk");
            if(chunkFile.exists()){
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chunkFile));
                Chunk chunk = (Chunk)objectInputStream.readObject();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
