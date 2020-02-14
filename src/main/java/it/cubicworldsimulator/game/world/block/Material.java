package it.cubicworldsimulator.game.world.block;

public class Material {

    private final byte id;
    private final String name;
    private final BlockTexture blockTexture;

    public Material(byte id, String name, BlockTexture blockTexture) {
        this.id = id;
        this.name = name;
        this.blockTexture = blockTexture;
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
}
