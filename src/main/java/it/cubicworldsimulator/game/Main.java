package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.gui.GuiFactory;
import it.cubicworldsimulator.game.gui.LauncherGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWVidMode;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final GuiFactory guiFactory = new GuiFactory();

    public static void main(String[] args) {
        try {
            logger.debug("Platform: " + System.getProperty("os.name"));
            logger.trace("Game launcher started...");
            guiFactory.createGui("CubicWorldSimulator Launcher");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
