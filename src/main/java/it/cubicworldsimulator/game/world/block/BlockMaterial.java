package it.cubicworldsimulator.game.world.block;

public class BlockMaterial {

    private final byte id;
    private final String name;
    private final BlockTexture blockTexture;
    private final boolean transparent;

    /**
     * @param id block id
     * @param name block name
     * @param blockTexture block texture
     * @param transparent block transparency (boolean)
     */
    public BlockMaterial(byte id, String name, BlockTexture blockTexture, boolean transparent) {
        this.id = id;
        this.name = name;
        this.blockTexture = blockTexture;
        this.transparent = transparent;
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
     * Gets the block transparency
     * @return block transparency
     */
    public boolean isTransparent() {
        return transparent;
    }
}
