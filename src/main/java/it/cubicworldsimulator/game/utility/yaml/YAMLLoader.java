package it.cubicworldsimulator.game.utility.yaml;

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
import java.util.Map;

public class YAMLLoader {

    private static final Logger logger = LogManager.getLogger(YAMLLoader.class);

    private final Load loader;

    public YAMLLoader() throws FileNotFoundException {
        loader = new Load(new LoadSettingsBuilder().setLabel("loader").build());
    }

    public YAMLComponent loadFile(String fileName) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(fileName);
        return new YAMLComponent(loader.loadFromInputStream(inputStream));
    }

}
