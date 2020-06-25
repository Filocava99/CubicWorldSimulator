package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.io.*;
import java.util.*;

public class ChunkLoader {

    private static final Logger logger = LogManager.getLogger(ChunkLoader.class);
    private final String chunkFolderPath;

    /**
     * @param worldName world name
     */
    public ChunkLoader(String worldName) {
        chunkFolderPath = Constants.installationFolder + File.separator + "data" + File.separator + worldName + File.separator + "chunks";
        logger.info(chunkFolderPath);
    }

    /**
     * Loads a chunk column from disk
     * @param x x coordinate
     * @param z z coordinate
     * @return the loaded chunkColumn
     */
    public Optional<ChunkColumn> loadChunkColumn(float x, float z){
        Optional<ChunkColumn> chunkColumnOptional = Optional.empty();
        try{
            File chunkFolder = new File(chunkFolderPath);
            chunkFolder.mkdirs();
            File chunkFile = new File(chunkFolderPath + File.separator + x + "_" + z + ".chunk");
            ChunkColumn chunkColumn = null;
            if(chunkFile.exists()){
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chunkFile));
                chunkColumn = (ChunkColumn) objectInputStream.readObject();
            }
            chunkColumnOptional = Optional.ofNullable(chunkColumn);
        } catch (Exception e) {
            logger.error(e);
        }
        return chunkColumnOptional;
    }

    public Optional<ChunkColumn> loadChunkColumn(Vector2f position){
        return loadChunkColumn(position.x, position.y);
    }

    /**
     * Saves a chunk column on disk
     * @param chunkColumn chunkColumn to be saved
     */
    public void saveChunkColumn(ChunkColumn chunkColumn){
        try{
            File chunkFolder = new File(chunkFolderPath);
            chunkFolder.mkdirs();
            File chunkFile = new File(chunkFolderPath + File.separator + (int)chunkColumn.getPosition().x + "_" + (int)chunkColumn.getPosition().y + ".chunk");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(chunkFile));
            objectOutputStream.writeObject(chunkColumn);
        } catch (Exception e) {

            logger.error(e);
        }
    }

    /**
     * Returns a set of the coordinates of the already generated chunk columns
     * @return set of coordinates
     */
    public Set<Vector2f> getAlreadyGeneratedChunkColumns(){
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
