package it.cubicworldsimulator.game.world;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class WorldManager {

    private static final Logger logger = LogManager.getLogger(WorldManager.class);

    private final World world;
    private final MultiKeyMap blockTypes = new MultiKeyMap();

    public WorldManager(World world) {
        loadBlockTypes();
        this.world = world;
    }

    private void loadBlockTypes() {
        YamlMapping yamlMapping = null;
        try {
            yamlMapping = Yaml.createYamlInput(new File("src/main/resources/default.yml")).readYamlMapping();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.debug(yamlMapping.toString());
        Material dirt = new Material((byte) 1,"dirt",new BlockTexture(0,null));
        blockTypes.put(1,"dirt",dirt);
    }

    //TODO Creare metodi espliciti in modo da ritornare il valore senza ritornare la mappa
    public MultiKeyMap<MultiKey, Material> getBlockTypes(){
        return blockTypes;
    }
}
