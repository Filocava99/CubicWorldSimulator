package it.cubicworldsimulator.game.world.block;

public class BlockMaterial {

    private final byte id;
    private final String name;
    private final BlockTexture blockTexture;
    private final boolean transparent;

    public BlockMaterial(byte id, String name, BlockTexture blockTexture, boolean transparent) {
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
