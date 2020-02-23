package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.engine.graphic.MeshMaterial;
import it.cubicworldsimulator.engine.graphic.Texture;
import it.cubicworldsimulator.engine.loader.TextureLoader;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;
import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import it.cubicworldsimulator.game.world.chunk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3i;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class WorldManager{

    private static final Logger logger = LogManager.getLogger(WorldManager.class);
    private final int renderingDistance = 8;

    private final World world;
    private final ChunkGenerator chunkGenerator;
    private final ChunkLoader chunkLoader;
    private final Map<Object,Material> blockTypes = new HashMap<>();
    private final Set<Vector2f> alreadyGeneratedChunksColumns;

    private String textureFile;
    private MeshMaterial worldTexture;

    private final Set<ChunkMesh> activeMeshes = new LinkedHashSet<>();

    public WorldManager(World world) {
        loadBlockTypes();
        this.world = world;
        TextureLoader loader = new TextureLoaderImpl();
        chunkGenerator = new ChunkGenerator(world.getSeed(), this);
        chunkLoader = new ChunkLoader(world.getName());
        alreadyGeneratedChunksColumns = chunkLoader.getAlreadyGeneratedChunkColumns(world.getName());
        try{
            worldTexture = new MeshMaterial(loader.loadTexture(textureFile));
        }catch (Exception e){
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    //TODO Controllare le perfomances, non vorrei che si inchiodasse se un giocatore fa avanti e indietro fra due chunk
    public void updateActiveChunks(Vector3i chunkPosition){
        new Thread(() -> {
            unloadOldChunks(chunkPosition);
            loadNewChunks(chunkPosition);
        }).start();
    }

    private void unloadOldChunks(Vector3i chunkPosition){
        //TODO
        Queue<Vector2f> dumpQueue = new LinkedBlockingQueue<>();
        var iterator = world.getActiveChunks().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            Vector2f chunkColumnPosition = entry.getValue().getPosition();
            if (chunkColumnPosition.x <chunkPosition.x - renderingDistance || chunkColumnPosition.x > chunkPosition.x + renderingDistance || chunkColumnPosition.y < chunkPosition.z - renderingDistance || chunkColumnPosition.y > chunkPosition.z + renderingDistance) {
                ChunkColumn chunkColumn = entry.getValue();
                chunkLoader.saveChunkColumn(chunkColumn);
                for(Chunk chunk : chunkColumn.getChunks()){
                    Game.unloadCommands.put(chunk,new OpenGLUnloadChunkCommand(chunk));
                    Game.loadCommands.remove(chunk);
                }
                dumpQueue.add(entry.getKey());
            }
        }
        while(!dumpQueue.isEmpty()){
            world.getActiveChunks().remove(dumpQueue.poll());
        }
    }

    private void loadNewChunks(Vector3i chunkPosition){
        //TODO
    }

    public ChunkColumn loadChunkColumn(Vector2f position){
        ChunkColumn chunkColumn;
        if(alreadyGeneratedChunksColumns.contains(position)){
            chunkColumn = chunkLoader.loadChunkColumn(position).orElse(chunkGenerator.generateChunkColumn((int)position.x,(int)position.y));
        }else{
            chunkColumn = chunkGenerator.generateChunkColumn((int)position.x,(int)position.y);
        }
        return chunkColumn;
    }

    public void renderChunkColumn(Vector2f position){
        ChunkColumn chunkColumn = loadChunkColumn(position);
        ChunkMesh[] chunkMeshes = new ChunkMesh[Constants.chunksPerColumn];
        for(int i = 0; i < chunkColumn.getChunks().length; i++){
            ChunkMesh chunkMesh = new ChunkMesh(chunkColumn.getChunks()[i],blockTypes,worldTexture);
            chunkMesh.prepareVAOContent();
            chunkMeshes[i] = chunkMesh;
        }
        activeMeshes.addAll(List.of(chunkMeshes));
    }

    private void loadBlockTypes() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/resources/default.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Load load = new Load(new LoadSettingsBuilder().setLabel("test").build());
        Map<String, Object> textureConfig = (Map<String, Object>) load.loadFromInputStream(inputStream);
        textureFile = textureConfig.get("file").toString();
        float textureStep = Float.parseFloat(textureConfig.get("step").toString());
        Map<String, Object> blocksList = (Map<String, Object>) textureConfig.get("blocks");
        blocksList.entrySet().stream().forEach(entry -> {
            String blockName = entry.getKey();
            Map<String, Object> blockInfo = (Map<String, Object>) entry.getValue();
            byte blockId = Byte.parseByte(blockInfo.get("id").toString());
            Map<String, Object> blockTextureInfo = (Map<String, Object>) blockInfo.get("textures");
            Material material = null;
            if (blockTextureInfo != null) {
                Vector2f[] coords = new Vector2f[6];
                var iterator = blockTextureInfo.entrySet().iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    if(i==6){
                        logger.warn("Found more than six coordinates for block '" + blockName + "' !");
                        break;
                    }
                    Map<String, Object> faceInfo = (Map<String, Object>) iterator.next().getValue();
                    float x = Float.parseFloat(faceInfo.get("x").toString()) * textureStep;
                    float y = Float.parseFloat(faceInfo.get("y").toString()) * textureStep;
                    coords[i] = new Vector2f(x, y);
                    i++;
                }
                material = new Material(blockId,blockName,new BlockTexture(textureStep, coords));
            }
            if(material == null){
                material = new Material(blockId,blockName,null);
            }
            blockTypes.put(blockName, material);
            blockTypes.put(blockId, material);
        });
    }

    public Map<Object, Material> getBlockTypes() {
        return Collections.unmodifiableMap(blockTypes);
    }

    public Set<ChunkMesh> getActiveMeshes() {
        return Collections.unmodifiableSet(activeMeshes);
    }
}
