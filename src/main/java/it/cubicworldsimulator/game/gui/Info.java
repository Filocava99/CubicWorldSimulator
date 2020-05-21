package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Panel;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class Info extends GenericGui {
    public Info() {
        super(0, 0,
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height(),
                new LauncherFactory());
        createGui();
    }

    @Override
    protected void createGui() {
        Panel info = new Panel(0, 0, (float)  Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height());
        guiFactory.createLabel("Info", new Vector2f(50, 50), info);
        add(info);
        changeTheme();
    }

    @Override
    public String getTitle() {
        return "Info CubicWorldSimulator";
    }

    @Override
    public void changeTheme() {
        flatColoredTheme();
    }
}
