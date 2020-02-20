package it.cubicworldsimulator.engine.graphic;

public class MeshMaterial {

    private final Texture texture;

    public MeshMaterial(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
