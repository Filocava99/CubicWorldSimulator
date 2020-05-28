package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.graphic.Texture.TextureFactory;
import it.cubicworldsimulator.engine.graphic.Texture.TextureFactoryImpl;
import it.cubicworldsimulator.game.CommandsQueue;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.utility.yaml.YAMLComponent;
import it.cubicworldsimulator.game.utility.yaml.YAMLLoader;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.BlockMaterial;
import it.cubicworldsimulator.game.world.chunk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WorldManager {

    private static final Logger logger = LogManager.getLogger(WorldManager.class);
    private final int renderingDistance = 8;

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

    public WorldManager(World world, CommandsQueue commandsQueue) {
        this.world = world;
        this.commandsQueue = commandsQueue;
        this.chunkGenerator = new ChunkGenerator(world.getSeed(), this);
        this.chunkLoader = new ChunkLoader(world.getName());
        this.alreadyGeneratedChunksColumns = chunkLoader.getAlreadyGeneratedChunkColumns(world.getName());
        TextureFactory textureFactory = new TextureFactoryImpl();
        try {
            loadConfig("src/main/resources/default.yml");
            worldTexture = new Material(textureFactory.createTexture(textureFile));
        } catch (Exception e) {
            logger.error(e);
            System.exit(1);
        }
    }

    //TODO Dubbio sulle perfomance. Bisogna fare profiling
    public void updateActiveChunksAsync(Vector3i chunkPosition) {
        new Thread(() -> {
            updateActiveChunksSync(chunkPosition);
        }).start();
    }

    public void updateActiveChunksSync(Vector3i chunkPosition) {
        unloadOldChunks(chunkPosition);
        loadNewChunks(chunkPosition);
    }

    private void unloadOldChunks(Vector3i chunkPosition) {
        Queue<Vector2f> dumpQueue = new LinkedBlockingQueue<>();
        for (Map.Entry<Vector2f, ChunkColumn> entry : world.getActiveChunks().entrySet()) {
            Vector2f chunkColumnPosition = entry.getValue().getPosition();
            if (chunkColumnPosition.x < chunkPosition.x - renderingDistance || chunkColumnPosition.x > chunkPosition.x + renderingDistance || chunkColumnPosition.y < chunkPosition.z - renderingDistance || chunkColumnPosition.y > chunkPosition.z + renderingDistance) {
                ChunkColumn chunkColumn = entry.getValue();
                chunkLoader.saveChunkColumn(chunkColumn);
                for (Chunk chunk : chunkColumn.getChunks()) {
                    commandsQueue.addUnloadCommand(chunk.getPosition(), new OpenGLUnloadChunkCommand(chunkMeshes.get(chunk.getPosition())));
                    chunkMeshes.remove(chunk.getPosition());
                }
                dumpQueue.add(entry.getKey());
            }
        }
        while (!dumpQueue.isEmpty()) {
            world.getActiveChunks().remove(dumpQueue.poll());
        }
    }

    private void loadNewChunks(Vector3i chunkPosition) {
        var activeChunks = world.getActiveChunks();
        for (int x = chunkPosition.x - renderingDistance; x <= chunkPosition.x + renderingDistance; x++) {
            for (int z = chunkPosition.z - renderingDistance; z <= chunkPosition.z + renderingDistance; z++) {
                Vector2f newChunkCoord = new Vector2f(x, z);
                if (!activeChunks.containsKey(newChunkCoord)) {
                    ChunkColumn chunkColumn = loadChunkColumn(newChunkCoord);
                    alreadyGeneratedChunksColumns.add(newChunkCoord);
                    for (int i = 15; i >= 0; i--) {
                        Chunk chunk = chunkColumn.getChunks()[i];
                        ChunkMesh chunkMesh = new ChunkMesh(chunk, blockTypes, worldTexture);
                        chunkMesh.prepareVAOContent();
                        commandsQueue.addLoadCommand(chunk.getPosition(), new OpenGLLoadChunkCommand(chunkMesh));
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

    private void loadTextureConfig(YAMLComponent root){
        textureFile = root.getString("file");
        textureStep = root.getFloat("step");
    }

    private void loadBlockTypes(YAMLComponent root)  {
        YAMLComponent blocksList = root.getYAMLComponent("blocks");
        blocksList.getEntrySet().forEach(entry -> {
            String blockName = entry.getKey();
            YAMLComponent blockInfo = new YAMLComponent(entry.getValue());
            byte blockId = blockInfo.getByte("id");
            boolean transparency = blockInfo.getBoolean("transparent");
            YAMLComponent blockTextureInfo = blockInfo.getYAMLComponent("textures");
            BlockMaterial material = null;
            if (blockTextureInfo != null) {
                Vector2f[] coords = new Vector2f[6];
                var iterator = blockTextureInfo.getEntrySet().iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    if (i == 6) {
                        logger.warn("Found more than six coordinates for block '" + blockName + "' !");
                        break;
                    }
                    YAMLComponent faceInfo = new YAMLComponent(iterator.next().getValue());
                    float x = faceInfo.getFloat("x");
                    float y = faceInfo.getFloat("y");
                    coords[i] = new Vector2f(x, y);
                    i++;
                }
                material = new BlockMaterial(blockId, blockName, new BlockTexture(textureStep, coords), transparency);
            }
            if (material == null) {
                material = new BlockMaterial(blockId, blockName, null, transparency);
            }
            blockTypes.put(blockName, material);
            blockTypes.put(blockId, material);
        });
    }

    public Map<Object, BlockMaterial> getBlockTypes() {
        return Collections.unmodifiableMap(blockTypes);
    }

    public Collection<ChunkMesh> getChunkMeshes() {
        return Collections.unmodifiableCollection(chunkMeshes.values());
    }
}
