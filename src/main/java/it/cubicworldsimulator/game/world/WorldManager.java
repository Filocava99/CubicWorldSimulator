package it.cubicworldsimulator.game.world;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Texture;
import it.cubicworldsimulator.engine.loader.TextureLoader;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorldManager {

    private static final Logger logger = LogManager.getLogger(WorldManager.class);

    private final World world;
    private final Map<Object,Material> blockTypes = new HashMap<>();
    private String textureFile;
    public Mesh mesh;
    public Texture worldTexture;

    public WorldManager(World world) {
        loadBlockTypes();
        this.world = world;
        TextureLoader loader = new TextureLoaderImpl();
        try{
            worldTexture = loader.loadTexture(textureFile);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
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
}
