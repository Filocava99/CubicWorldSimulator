package it.cubicworldsimulator.game.world.block;

public class BlockBuilder {

    private byte id = 0;
    private String name = "";
    private BlockTexture blockTexture;
    private boolean transparent = false;

    /**
     * Sets the block id
     * @param id block id
     * @return BlockBuilder
     */
    public BlockBuilder setId(byte id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the block name
     * @param name block name
     * @return BlockBuilder
     */
    public BlockBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the block texture
     * @param blockTexture block texture
     * @return BlockBuilder
     */
    public BlockBuilder setBlockTexture(BlockTexture blockTexture) {
        this.blockTexture = blockTexture;
        return this;
    }

    /**
     * Sets the block transparency
     * @param transparent boolean
     * @return BlockBuilder
     */
    public BlockBuilder setTransparency(boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    /**
     * Returns an instance of BlockMaterial based on the previously given values
     * @return BlockMaterial
     */
    public BlockMaterial build(){
        return new BlockMaterial(id, name, blockTexture, transparent);
    }

    /**
     * Gets the block id
     * @return block id
     */
    public byte getId() {
        return id;
    }

    /**
     * Gets the block name
     * @return block name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the block texture
     * @return block texture
     */
    public BlockTexture getBlockTexture() {
        return blockTexture;
    }

    /**
     * Gets the transparency flag
     * @return boolean
     */
    public boolean isTransparency() {
        return transparent;
    }
}
