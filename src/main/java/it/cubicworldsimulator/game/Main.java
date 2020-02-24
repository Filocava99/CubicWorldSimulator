package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.gui.GuiFactory;
import it.cubicworldsimulator.game.gui.LauncherGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final GuiFactory guiFactory = new GuiFactory();

    public static void main(String[] args) {
        final int width = 800;
        final int height = 800;

        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.trace("Game launcher started...");
            final var size = new Vector2i(width, height);
            guiFactory.createGui(new LauncherGui(size), size, "CubicWorldSimulator Launcher");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
