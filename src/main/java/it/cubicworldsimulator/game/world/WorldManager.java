package it.cubicworldsimulator.game.world;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class WorldManager {

    private final World world;
    private final MultiKeyMap blockTypes = new MultiKeyMap();

    public WorldManager(World world) {
        this.world = world;
    }

    private void loadBlockTypes() throws IOException {
        YamlMapping yamlMapping = Yaml.createYamlInput("default.yml").readYamlMapping();
        yamlMapping.string("prova");
        Material dirt = new Material(1,"dirt",new BlockTexture(0,null));
        blockTypes.put(1,"dirt",dirt);
    }

    //TODO Creare metodi espliciti in modo da ritornare il valore senza ritornare la mappa
    public MultiKeyMap<MultiKey, Material> getBlockTypes(){
        return blockTypes;
    }
}
