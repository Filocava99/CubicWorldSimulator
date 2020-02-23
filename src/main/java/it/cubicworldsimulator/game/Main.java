package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.game.gui.LauncherGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        boolean debug = false;
        boolean vsync = true;

        //TODO Aggiungere vSync come parametro di avvio
        if (args != null && args.length > 0) {
            if(args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("true")) {
                debug = Boolean.parseBoolean(args[0]);
            }
        }
        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.info("VSync: " + vsync);
            logger.debug("Debug mode: " + debug);
            logger.trace("Game running...");
            GameEngine gameEngine = new GameEngine("CubicWorldSimulator",
                    true, new Game(), false);
            final Vector2i size = new Vector2i(800, 750);
            GuiFactory.createGui(new LauncherGui(size), size, "CubicWorldSimulator Launcher");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
