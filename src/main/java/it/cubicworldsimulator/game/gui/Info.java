package it.cubicworldsimulator.game.gui;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;

public class Info extends GenericGui {
    public Info() {
        super(0, 0,
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).width(),
                Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())).height());
        createGui();
    }

    @Override
    protected void createGui() {
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
