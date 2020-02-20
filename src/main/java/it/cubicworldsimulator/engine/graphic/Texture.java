package it.cubicworldsimulator.engine.graphic;

public class Texture {
    private final int id;
    private final int width;
    private final int height;

    public Texture(int id, int width, int height) {
        this.id = id;
        this.width=width;
        this.height=height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }
}
