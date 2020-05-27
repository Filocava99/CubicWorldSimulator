package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.gui.MyGuiContainer;
import it.cubicworldsimulator.game.gui.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.trace("Game launcher started...");
            new MyGuiContainer().init(Arrays.asList(new Launcher()));
        } catch (Exception e) {
            logger.error(e);
            System.exit(-1);
        }
    }
}
