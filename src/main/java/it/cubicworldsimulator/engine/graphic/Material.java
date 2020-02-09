package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.Texture;

public class Material {

    private final Texture texture;

    public Material(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
