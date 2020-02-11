package it.cubicworldsimulator.game.world.block;

public class Material {

    private final int id;
    private final String name;
    private final BlockTexture blockTexture;

    public Material(int id, String name, BlockTexture blockTexture) {
        this.id = id;
        this.name = name;
        this.blockTexture = blockTexture;
    }
}
