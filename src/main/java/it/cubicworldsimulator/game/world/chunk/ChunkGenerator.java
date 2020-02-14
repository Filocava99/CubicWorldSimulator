package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.game.utility.math.OpenSimplexNoise;
import it.cubicworldsimulator.game.world.WorldManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

public class ChunkGenerator {
    private final OpenSimplexNoise noise;
    private final WorldManager worldManager;
    private int waterLevel = 30;
    private int stoneLevel = 60;

    public ChunkGenerator(long seed, WorldManager worldManager) {
        this.noise = new OpenSimplexNoise(seed);
        this.worldManager = worldManager;
    }

    public ChunkColumn generateChunkColumn(int chunkX, int chunkZ){
        //System.out.println("Generazione colonna x:  " + chunkX + "  z:  " + chunkZ);
        byte[] blocks = new byte[4096*16];
        Chunk[] chunks = new Chunk[16];
        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++){
                double height = noise.sumOcatave(16,((chunkX<<4)+x),((chunkZ<<4)+z),0.5, 0.0095, 0, 255)/4;
                //System.out.println(height);
                for(int y = 0; y < height; y++){
                    if(y <= stoneLevel){
                        blocks[(x)+(y*256)+(z*16)] = worldManager.getBlockTypes().get("stone").getId();
                    }else{
                        blocks[(x)+(y*256)+(z*16)] = worldManager.getBlockTypes().get("dirt").getId();
                    }
                }
                blocks[(x)+((int)height<<8)+(z<<4)] = worldManager.getBlockTypes().get("grass").getId();
                for(int y = (int)height+1; y<256; y++){
                    if(y <= waterLevel){
                        blocks[(x)+(y<<8)+(z<<4)] = worldManager.getBlockTypes().get("water").getId();
                    }else{
                        blocks[(x)+(y<<8)+(z<<4)] = worldManager.getBlockTypes().get("air").getId();
                    }
                }
            }
        }
        //TODO Fix magic numbers | 4096 * 16 = 65536
        for(int i = 0; i < 16; i++){
            chunks[i] = new Chunk(Arrays.copyOfRange(blocks,4096*i,4096*i+4096), new Vector3f(chunkX,i,chunkZ));
        }
        //System.out.println("Fine generazione");
        return  new ChunkColumn(chunks, new Vector2f(chunkX,chunkZ));
    }
}
