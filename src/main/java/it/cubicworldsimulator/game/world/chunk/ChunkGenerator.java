package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.game.utility.math.OpenSimplexNoise;
import it.cubicworldsimulator.game.utility.math.SerializableVector3f;
import it.cubicworldsimulator.game.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.Random;

public class ChunkGenerator {
    private static final Logger logger = LogManager.getLogger(ChunkGenerator.class);

    private final OpenSimplexNoise noise;
    private final WorldManager worldManager;
    private int waterLevel = 30;
    private int stoneLevel = 60;

    public ChunkGenerator(long seed, WorldManager worldManager) {
        this.noise = new OpenSimplexNoise(seed);
        this.worldManager = worldManager;
    }

    public ChunkColumn generateChunkColumn(int chunkX, int chunkZ) {
        //System.out.println("Generazione colonna x:  " + chunkX + "  z:  " + chunkZ);
        byte[] blocks = new byte[4096 * 16];
        Chunk[] chunks = new Chunk[16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double height = noise.sumOcatave(16, ((chunkX << 4) + x), ((chunkZ << 4) + z), 0.5, 0.0095, 0, 255) / 4;
                //System.out.println(height);
                for (int y = 0; y < height; y++) {
                    if (y <= stoneLevel) {
                        blocks[(x) + (y * 256) + (z * 16)] = worldManager.getBlockTypes().get("stone").getId();
                    } else {
                        blocks[(x) + (y * 256) + (z * 16)] = worldManager.getBlockTypes().get("dirt").getId();
                    }
                }
                blocks[(x) + ((int) height << 8) + (z << 4)] = worldManager.getBlockTypes().get("grass").getId();
                for (int y = (int) height + 1; y < 256; y++) {
                    if (y <= waterLevel) {
                        blocks[(x) + (y << 8) + (z << 4)] = worldManager.getBlockTypes().get("water").getId();
                    } else {
                        blocks[(x) + (y << 8) + (z << 4)] = worldManager.getBlockTypes().get("air").getId();
                    }
                }
            }
        }
        //TODO Fix magic numbers | 4096 * 16 = 65536
        for (int i = 0; i < 16; i++) {
            chunks[i] = new Chunk(Arrays.copyOfRange(blocks, 4096 * i, 4096 * i + 4096), new SerializableVector3f(chunkX, i, chunkZ));
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
            int y = getHeight(new Vector2i(x,z), chunkColumn);
            Vector3i treePos = new Vector3i(x, y, z);
            int height = random.nextInt()%2+4;
            if(isSpaceAvailableForTree(chunkColumn,treePos, height)){
                growTree(chunkColumn, treePos, height);
            }
        }
    }

    private int getHeight(Vector2i coord, ChunkColumn chunkColumn){
        for(int i = 1; i < 256; i++){
            byte blockId = chunkColumn.getBlock(new Vector3i(coord.x, i, coord.y));
            if(blockId == worldManager.getBlockTypes().get("air").getId()){
                return Math.max(0,i);
            }
        }
        return 255;
    }

    private boolean isSpaceAvailableForTree(ChunkColumn chunkColumn, Vector3i coord, int height){
        if(coord.y>0){
            if(chunkColumn.getBlock(new Vector3i(coord.x, coord.y-1, coord.z)) == worldManager.getBlockTypes().get("water").getId()){
                return false;
            }
        }
        return coord.y + height <= 255;
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
    //TODO Pattern dell'albero da file o con classe apposita
    private void growTree(ChunkColumn chunk, Vector3i pos, int height) {
        byte logId = worldManager.getBlockTypes().get("log").getId();
        byte leafId = worldManager.getBlockTypes().get("leaf").getId();
        //Max layer
       // if (height + pos.y)

        for(int i = 0; i <= height; i++){
            //tronco
            chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z), logId);
            if(i>height-3){
                chunk.setBlock(new Vector3i(pos.x-1, pos.y + i, pos.z), leafId);
                chunk.setBlock(new Vector3i(pos.x+1, pos.y + i, pos.z), leafId);
                chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z-1), leafId);
                chunk.setBlock(new Vector3i(pos.x, pos.y + i, pos.z+1), leafId);
            }
        }
        //max
        chunk.setBlock(new Vector3i(pos.x, pos.y + height, pos.z), leafId);

        //max-1

        Random random = new Random();
        if(random.nextBoolean()){
            chunk.setBlock(new Vector3i(pos.x-1, pos.y + height, pos.z-1), leafId);
        }
        if(random.nextBoolean()){
            chunk.setBlock(new Vector3i(pos.x-1, pos.y + height, pos.z+1), leafId);
        }
        if(random.nextBoolean()){
            chunk.setBlock(new Vector3i(pos.x+1, pos.y + height, pos.z-1), leafId);
        }
        if(random.nextBoolean()){
            chunk.setBlock(new Vector3i(pos.x+1, pos.y + height, pos.z+1), leafId);
        }
    }
}
