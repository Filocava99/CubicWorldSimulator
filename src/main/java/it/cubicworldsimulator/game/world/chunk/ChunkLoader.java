package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.io.*;
import java.util.Optional;

public class ChunkLoader {

    private static final Logger logger = LogManager.getLogger(ChunkLoader.class);
    private final String chunkFolderPath = Constants.installationFolder + File.pathSeparator + "data" + File.pathSeparator + "chunks";

    public Optional<ChunkColumn> loadChunkColumn(float x, float y){
        Optional<ChunkColumn> chunkColumnOptional = Optional.empty();
        try{
            File chunkFolder = new File(chunkFolderPath);
            chunkFolder.mkdirs();
            File chunkFile = new File(chunkFolderPath + File.pathSeparator + x + "_" + y + ".chunk");
            ChunkColumn chunkColumn = null;
            if(chunkFile.exists()){
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chunkFile));
                chunkColumn = (ChunkColumn) objectInputStream.readObject();
            }
            chunkColumnOptional = Optional.ofNullable(chunkColumn);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return chunkColumnOptional;
    }

    public Optional<ChunkColumn> loadChunkColumn(Vector2f position){
        return loadChunkColumn(position.x, position.y);
    }

    public void saveChunkColumn(ChunkColumn chunkColumn){
        try{
            File chunkFolder = new File(chunkFolderPath);
            chunkFolder.mkdirs();
            File chunkFile = new File(chunkFolderPath + File.pathSeparator + chunkColumn.getPosition().x + "_" + chunkColumn.getPosition().y + ".chunk");
            if(chunkFile.exists()){
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(chunkFile));
                objectOutputStream.writeObject(chunkColumn);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    //TODO Creare un metodo per salvare un solo chunk

}
