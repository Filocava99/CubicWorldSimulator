package it.cubicworldsimulator.game.world.block;

import it.cubicworldsimulator.engine.graphic.MeshMaterial;

public class Material {

    private final byte id;
    private final String name;
    private final BlockTexture blockTexture;
    private final boolean transparent;

    public Material(byte id, String name, BlockTexture blockTexture, boolean transparent) {
        this.id = id;
        this.name = name;
        this.blockTexture = blockTexture;
        this.transparent = transparent;
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BlockTexture getBlockTexture() {
        return blockTexture;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
