package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.graphic.texture.TextureFactory;
import it.cubicworldsimulator.engine.graphic.texture.TextureFactoryImpl;
import it.cubicworldsimulator.game.CommandsQueue;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.utility.yaml.YAMLComponent;
import it.cubicworldsimulator.game.utility.yaml.YAMLLoader;
import it.cubicworldsimulator.game.world.block.BlockBuilder;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.BlockMaterial;
import it.cubicworldsimulator.game.world.chunk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WorldManager {

    private static final Logger LOGGER = LogManager.getLogger(WorldManager.class);
    private static final int LOADED_CHUNKS_RADIUS = 8;

    private final World world;
    private final CommandsQueue commandsQueue;
    private final ChunkGenerator chunkGenerator;
    private final ChunkLoader chunkLoader;
    private final Map<Object, BlockMaterial> blockTypes = new HashMap<>();
    private final Set<Vector2f> alreadyGeneratedChunksColumns;
    private final ConcurrentHashMap<Vector3f, ChunkMesh> chunkMeshes = new ConcurrentHashMap<>();

    private String textureFile;
    private float textureStep;
    private Material worldTexture;

    /**
     * @param world world instance
     * @param commandsQueue OpenGL CommandsQueue instance (the same instance must be used in the main thread)
     */
    public WorldManager(World world, CommandsQueue commandsQueue) {
        this.world = world;
        this.commandsQueue = commandsQueue;
        this.chunkGenerator = new ChunkGenerator(new DefaultGenerationAlgorithm(world.getSeed(), blockTypes));
        this.chunkLoader = new ChunkLoader(world.getName());
        this.alreadyGeneratedChunksColumns = chunkLoader.getAlreadyGeneratedChunkColumns();
        TextureFactory textureFactory = new TextureFactoryImpl();
        try {
            loadConfig("default.yml");
            worldTexture = new Material(textureFactory.createTexture(textureFile));
        } catch (Exception e) {
            LOGGER.error(e);
            System.exit(1);
        }
    }
    
    //TODO Dubbio sulle perfomance. Bisogna fare profiling
    /**
     * Updates on another thread the map of the currently loaded chunks based on the new player position
     * @param chunkPosition position of the chunk where the player currently is
     */
    public void updateActiveChunksAsync(Vector3i chunkPosition) {
        new Thread(() -> {
            updateActiveChunksSync(chunkPosition);
        }).start();
    }
    /**
     * Updates  the map of the currently loaded chunks based on the new player position
     * @param chunkPosition position of the chunk where the player currently is
     */
    public void updateActiveChunksSync(Vector3i chunkPosition) {
        unloadOldChunks(chunkPosition);
        loadNewChunks(chunkPosition);
    }

    /**
     * Unloads the no more used chunks
     * @param chunkPosition position of the chunk where the player currently is
     */
    private void unloadOldChunks(Vector3i chunkPosition) {
        Queue<Vector2f> dumpQueue = new LinkedBlockingQueue<>();
        for (Map.Entry<Vector2f, ChunkColumn> entry : world.getActiveChunks().entrySet()) {
            Vector2f chunkColumnPosition = entry.getValue().getPosition();
            if (chunkColumnPosition.x < chunkPosition.x - LOADED_CHUNKS_RADIUS || chunkColumnPosition.x > chunkPosition.x + LOADED_CHUNKS_RADIUS || chunkColumnPosition.y < chunkPosition.z - LOADED_CHUNKS_RADIUS || chunkColumnPosition.y > chunkPosition.z + LOADED_CHUNKS_RADIUS) {
                ChunkColumn chunkColumn = entry.getValue();
                chunkLoader.saveChunkColumn(chunkColumn);
                for (Chunk chunk : chunkColumn.getChunks()) {
                    if(commandsQueue.containsLoadCommand(chunk.getPosition())){
                        commandsQueue.removeLoadCommand(chunk.getPosition());
                    }else{
                        commandsQueue.addUnloadCommand(chunk.getPosition(), new OpenGLUnloadChunkCommand(chunkMeshes.get(chunk.getPosition())));
                    }
                    chunkMeshes.remove(chunk.getPosition());
                }
                dumpQueue.add(entry.getKey());
            }
        }
        while (!dumpQueue.isEmpty()) {
            world.getActiveChunks().remove(dumpQueue.poll());
        }
    }

    /**
     * Loads the need chunks
     * @param chunkPosition position of the chunk where the player currently is
     */
    private void loadNewChunks(Vector3i chunkPosition) {
        var activeChunks = world.getActiveChunks();
        for (int x = chunkPosition.x - LOADED_CHUNKS_RADIUS; x <= chunkPosition.x + LOADED_CHUNKS_RADIUS; x++) {
            for (int z = chunkPosition.z - LOADED_CHUNKS_RADIUS; z <= chunkPosition.z + LOADED_CHUNKS_RADIUS; z++) {
                Vector2f newChunkCoord = new Vector2f(x, z);
                if (!activeChunks.containsKey(newChunkCoord)) {
                    ChunkColumn chunkColumn = loadChunkColumn(newChunkCoord);
                    alreadyGeneratedChunksColumns.add(newChunkCoord);
                    for (int i = 15; i >= 0; i--) {
                        Chunk chunk = chunkColumn.getChunks()[i];
                        ChunkMesh chunkMesh = new ChunkMesh(chunk, blockTypes, worldTexture);
                        chunkMesh.prepareVAOContent();
                        if(commandsQueue.containsUnloadCommand(chunk.getPosition())){
                            commandsQueue.removeUnloadCommand(chunk.getPosition());
                        }else{
                            commandsQueue.addLoadCommand(chunk.getPosition(), new OpenGLLoadChunkCommand(chunkMesh));
                        }
                        chunkMeshes.put(chunk.getPosition(), chunkMesh);
                    }
                    activeChunks.put(newChunkCoord, chunkColumn);
                }
            }
        }
    }

    private ChunkColumn loadChunkColumn(Vector2f position) {
        ChunkColumn chunkColumn;
        if (alreadyGeneratedChunksColumns.contains(position)) {
            chunkColumn = chunkLoader.loadChunkColumn(position).orElse(chunkGenerator.generateChunkColumn((int) position.x, (int) position.y));
        } else {
            chunkColumn = chunkGenerator.generateChunkColumn((int) position.x, (int) position.y);
        }
        return chunkColumn;
    }

    private void loadConfig(String configFileName) throws FileNotFoundException {
        YAMLLoader yamlLoader = new YAMLLoader();
        YAMLComponent root = yamlLoader.loadFile(configFileName);
        loadTextureConfig(root);
        loadBlockTypes(root);
    }

    private void loadTextureConfig(YAMLComponent root) {
        textureFile = root.getString("file");
        textureStep = root.getFloat("step");
    }

    private void loadBlockTypes(YAMLComponent root) {
        YAMLComponent blocksList = root.getYAMLComponent("blocks");
        blocksList.getEntrySet().forEach(entry -> {
            BlockBuilder blockBuilder = new BlockBuilder();
            YAMLComponent blockInfo = new YAMLComponent(entry.getValue());
            blockBuilder.setName(entry.getKey()).setId(blockInfo.getByte("id")).setTransparency(blockInfo.getBoolean("transparent"));
            YAMLComponent blockTextureInfo = blockInfo.getYAMLComponent("textures");
            BlockMaterial material;
            if (blockTextureInfo != null) {
                Vector2f[] coords = new Vector2f[6];
                var iterator = blockTextureInfo.getEntrySet().iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    if (i == 6) {
                        LOGGER.warn("Found more than six coordinates for block '" + blockBuilder.getName() + "' !");
                        break;
                    }
                    YAMLComponent faceInfo = new YAMLComponent(iterator.next().getValue());
                    float x = faceInfo.getFloat("x");
                    float y = faceInfo.getFloat("y");
                    coords[i] = new Vector2f(x, y);
                    i++;
                }
                blockBuilder.setBlockTexture(new BlockTexture(textureStep, coords));
            }
            material = blockBuilder.build();
            blockTypes.put(blockBuilder.getName(), material);
            blockTypes.put(blockBuilder.getId(), material);
        });
    }

    /**
     * Returns the map of the block types. You can use both the name(String) and the id*byte) as key
     * @return the map of the block types
     */
    public Map<Object, BlockMaterial> getBlockTypes() {
        return Collections.unmodifiableMap(blockTypes);
    }

    /**
     * Returns the meshes of the loaded chunks
     * @return an unmodifiable collection of the meshes of the loaded chunks
     */
    public Collection<ChunkMesh> getChunkMeshes() {
        return Collections.unmodifiableCollection(chunkMeshes.values());
    }

    /**
     * @return the world instance
     */
    public World getWorld() {
    	return this.world;
    }
}
