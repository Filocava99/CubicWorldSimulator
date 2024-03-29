package it.cubicworldsimulator.game.gui;

/**
 * It models a generic monitor property
 * @author Lorenzo Balzani
 */

public class MonitorProperty {
    private int height;
    private int width;
    private int refreshRate;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }
}
