package it.cubicworldsimulator.game.gui;

/**
 * @author Lorenzo Balzani
 */

public class MyMonitorProperty implements MonitorProperty {
    private int height;
    private int width;
    private int refreshRate;

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getRefreshRate() {
        return refreshRate;
    }

    @Override
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }
}
