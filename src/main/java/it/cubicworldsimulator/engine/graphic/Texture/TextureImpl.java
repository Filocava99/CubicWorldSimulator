package it.cubicworldsimulator.engine.graphic.Texture;

public class TextureImpl implements Texture {
    private final int id;
    private final int width;
    private final int height;

    public TextureImpl(int id, int width, int height) {
        this.id = id;
        this.width=width;
        this.height=height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getId() {
        return id;
    }
}
