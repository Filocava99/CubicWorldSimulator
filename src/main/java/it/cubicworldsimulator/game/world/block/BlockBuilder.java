package it.cubicworldsimulator.game.world.block;

public class BlockBuilder {

    private byte id;
    private String name;
    private BlockTexture blockTexture;
    private boolean transparent;

    public BlockBuilder setId(byte id) {
        this.id = id;
        return this;
    }

    public BlockBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BlockBuilder setBlockTexture(BlockTexture blockTexture) {
        this.blockTexture = blockTexture;
        return this;
    }

    public BlockBuilder setTransparency(boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    public BlockMaterial build(){
        return new BlockMaterial(id, name, blockTexture, transparent);
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

    public boolean isTransparency() {
        return transparent;
    }
}
