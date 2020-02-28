package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.MeshMaterial;
import it.cubicworldsimulator.engine.loader.TextureLoader;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;
import it.cubicworldsimulator.game.CommandsQueue;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import it.cubicworldsimulator.game.world.chunk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private final Map<Object, Material> blockTypes = new HashMap<>();
    private final Set<Vector2f> alreadyGeneratedChunksColumns;
    private final ConcurrentHashMap<Vector3f, ChunkMesh> chunkMeshes = new ConcurrentHashMap<>();

    private String textureFile;
    private MeshMaterial worldTexture;

    public WorldManager(World world, CommandsQueue commandsQueue) {
        this.world = world;
        this.commandsQueue = commandsQueue;
        this.chunkGenerator = new ChunkGenerator(world.getSeed(), this);
        this.chunkLoader = new ChunkLoader(world.getName());
        this.alreadyGeneratedChunksColumns = chunkLoader.getAlreadyGeneratedChunkColumns(world.getName());
        TextureLoader loader = new TextureLoaderImpl();
        loadBlockTypes();
        try {
            worldTexture = new MeshMaterial(loader.loadTexture(textureFile));
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    //TODO Controllare le perfomances, non vorrei che si inchiodasse se un giocatore fa avanti e indietro fra due chunk
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
        var iterator = world.getActiveChunks().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
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

    //TODO YAML loader class
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
        logger.debug(textureStep);
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
                    if (i == 6) {
                        logger.warn("Found more than six coordinates for block '" + blockName + "' !");
                        break;
                    }
                    Map<String, Object> faceInfo = (Map<String, Object>) iterator.next().getValue();
                    float x = Float.parseFloat(faceInfo.get("x").toString());
                    float y = Float.parseFloat(faceInfo.get("y").toString());
                    coords[i] = new Vector2f(x, y);
                    i++;
                }
                material = new Material(blockId, blockName, new BlockTexture(textureStep, coords));
            }
            if (material == null) {
                material = new Material(blockId, blockName, null);
            }
            blockTypes.put(blockName, material);
            blockTypes.put(blockId, material);
        });
    }

    public Map<Object, Material> getBlockTypes() {
        return Collections.unmodifiableMap(blockTypes);
    }

    public Collection<ChunkMesh> getChunkMeshes() {
        return Collections.unmodifiableCollection(chunkMeshes.values());
    }
}
