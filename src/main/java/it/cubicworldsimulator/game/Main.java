package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.gui.GenericGui;

import it.cubicworldsimulator.game.gui.GuiContainer;
import it.cubicworldsimulator.game.gui.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.trace("Game launcher started...");

            new GuiContainer().init(new Launcher());
        } catch (Exception e) {
            logger.error(e);
            System.exit(-1);
        }
    }
}
