package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.io.*;
import java.util.*;

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

    public Set<Vector2f> getAlreadyGeneratedChunkColumns(String worldName){
        Set<Vector2f> alreadyGeneratedChunkColumns = new HashSet<>();
        File chunkFolder = new File(chunkFolderPath);
        if(chunkFolder.exists()){
            Arrays.stream(Objects.requireNonNull(chunkFolder.listFiles(childFile -> childFile.getName().contains(".chunk")))).forEach(file -> {
                try {
                    String[] fileNameComponents = file.getName().split("\\.")[0].split("_");
                    int x = Integer.parseInt(fileNameComponents[0]);
                    int z = Integer.parseInt(fileNameComponents[1]);
                    alreadyGeneratedChunkColumns.add(new Vector2f(x, z));
                }catch (Exception e){
                    logger.warn("Invalid chunk file name: " + file.getName());
                }
            });
        }
        return alreadyGeneratedChunkColumns;
    }

}
