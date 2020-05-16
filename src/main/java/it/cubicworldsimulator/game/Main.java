package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.gui.GuiCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final GuiCreator guiCreator = new GuiCreator();

    public static void main(String[] args) {
        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.trace("Game launcher started...");
            guiCreator.createGui("CubicWorldSimulator Launcher");
        } catch (Exception e) {
            logger.error(e);
            System.exit(-1);
        }
    }
}
