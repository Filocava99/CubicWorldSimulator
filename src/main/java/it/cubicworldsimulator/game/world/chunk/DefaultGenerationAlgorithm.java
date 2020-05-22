package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.utility.math.OpenSimplexNoise;
import it.cubicworldsimulator.game.utility.math.SerializableVector3f;
import it.cubicworldsimulator.game.world.block.BlockMaterial;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class DefaultGenerationAlgorithm implements GenerationAlgorithm{

    private final OpenSimplexNoise noise;
    private final Map<Object, BlockMaterial> blockTypes;
    private static final int WATER_LEVEL = 30;
    private static final int STONE_LEVEL = 60;


    public DefaultGenerationAlgorithm(long seed, Map<Object, BlockMaterial> blockTypes) {
        this.noise = new OpenSimplexNoise(seed);
        this.blockTypes = blockTypes;
    }

    @Override
    public ChunkColumn generateChunkColumn(int chunkX, int chunkZ) {
        //System.out.println("Generazione colonna x:  " + chunkX + "  z:  " + chunkZ);
        byte[] blocks = new byte[4096 * 16];
        Chunk[] chunks = new Chunk[16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double height = noise.sumOcatave(16, ((chunkX << 4) + x), ((chunkZ << 4) + z), 0.5, 0.0095, 0, 255) / 4;
                //System.out.println(height);
                for (int y = 0; y < height; y++) {
                    if (y <= STONE_LEVEL) {
                        blocks[(x) + (y * 256) + (z * 16)] = blockTypes.get("stone").getId();
                    } else {
                        blocks[(x) + (y * 256) + (z * 16)] = blockTypes.get("dirt").getId();
                    }
                }
                blocks[(x) + ((int) height << 8) + (z << 4)] = blockTypes.get("grass").getId();
                for (int y = (int) height + 1; y < 256; y++) {
                    if (y <= WATER_LEVEL) {
                        blocks[(x) + (y << 8) + (z << 4)] = blockTypes.get("water").getId();
                    } else {
                        blocks[(x) + (y << 8) + (z << 4)] = blockTypes.get("air").getId();
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            chunks[i] = new Chunk(Arrays.copyOfRange(blocks, Constants.chunkTotalBlocks * i, Constants.chunkTotalBlocks * i + Constants.chunkTotalBlocks), new SerializableVector3f(chunkX, i, chunkZ));
        }
        //System.out.println("Fine generazione");
        ChunkColumn chunkColumn = new ChunkColumn(chunks, new Vector2f(chunkX, chunkZ));
        generateTrees(chunkColumn);
        return chunkColumn;
    }

    private void generateTrees(ChunkColumn chunkColumn) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = Math.abs(random.nextInt() % 12) + 2;
            int z = Math.abs(random.nextInt() % 12) + 2;
            int y = chunkColumn.getHeight(new Vector2i(x, z), blockTypes.get("air").getId());
            Vector3i treePos = new Vector3i(x, y, z);
            int height = random.nextInt() % 2 + 4;
            if (isSpaceAvailableForTree(chunkColumn, treePos, height)) {
                growTree(chunkColumn, treePos, height);
            }
        }
    }



    private boolean isSpaceAvailableForTree(ChunkColumn chunkColumn, Vector3i coord, int height) {
        if (coord.y > Constants.minHeight) {
            byte block = chunkColumn.getBlock(new Vector3i(coord.x, coord.y - 1, coord.z));
            if (block == blockTypes.get("water").getId() || block == blockTypes.get("log").getId()) {
                return false;
            }
        }
        return coord.y + height <= Constants.maxHeight;
    }

    /*
    L: max	max-1	max-2	max-3
	.....	.....	%###%	%###%
	..#..	.%#%.	#####	#####
	.###.	.#O#.	##O##	##O##
	..#..	.%#%.	#####	#####
	.....	.....	%###%	%###%

    Key:
    . air
    # leaves
    O tree trunk
    % contains leaves 50% of the time
     */
    private void growTree(ChunkColumn chunk, Vector3i pos, int height) {
        byte logId = blockTypes.get("log").getId();
        byte leafId = blockTypes.get("leaf").getId();
        //Max layer
        // if (height + pos.y)

        for (int i = 0; i <= height; i++) {
            //tronco
            chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z), logId);
            if (i > height - 3) {
                chunk.setBlock(new Vector3i(pos.x - 1, pos.y + i, pos.z), leafId);
                chunk.setBlock(new Vector3i(pos.x + 1, pos.y + i, pos.z), leafId);
                chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z - 1), leafId);
                chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z + 1), leafId);
            }
        }
        //max
        chunk.setBlock(new Vector3i(pos.x, pos.y + height, pos.z), leafId);

        //max-2
        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 1, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 1, pos.z + 1), leafId);
        chunk.setBlock(new Vector3i(pos.x + 1, pos.y + height - 1, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x + 1, pos.y + height - 1, pos.z + 1), leafId);

        chunk.setBlock(new Vector3i(pos.x - 2, pos.y + height - 1, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x - 2, pos.y + height - 1, pos.z), leafId);
        chunk.setBlock(new Vector3i(pos.x - 2, pos.y + height - 1, pos.z + 1), leafId);

        chunk.setBlock(new Vector3i(pos.x + 2, pos.y + height - 1, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x + 2, pos.y + height - 1, pos.z), leafId);
        chunk.setBlock(new Vector3i(pos.x + 2, pos.y + height - 1, pos.z + 1), leafId);

        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 1, pos.z - 2), leafId);
        chunk.setBlock(new Vector3i(pos.x, pos.y + height - 1, pos.z - 2), leafId);
        chunk.setBlock(new Vector3i(pos.x +1, pos.y + height - 1, pos.z - 2), leafId);

        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 1, pos.z + 2), leafId);
        chunk.setBlock(new Vector3i(pos.x, pos.y + height - 1, pos.z + 2), leafId);
        chunk.setBlock(new Vector3i(pos.x +1, pos.y + height - 1, pos.z + 2), leafId);


        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 2, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x - 1, pos.y + height - 2, pos.z + 1), leafId);
        chunk.setBlock(new Vector3i(pos.x + 1, pos.y + height - 2, pos.z - 1), leafId);
        chunk.setBlock(new Vector3i(pos.x + 1, pos.y + height - 2, pos.z + 1), leafId);

    }
}
