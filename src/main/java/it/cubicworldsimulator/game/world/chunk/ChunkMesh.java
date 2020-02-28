package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.MeshMaterial;
import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.Material;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class ChunkMesh implements Serializable {
    private static final Logger logger = LogManager.getLogger(ChunkMesh.class);

    private transient List<Vector3f> verticesList;
    private transient List<Float> verticesFloatList;
    private transient List<Integer> indicesList;
    private transient List<Float> uvsList;
    private transient List<Vector3f> normalsList;

    private final transient Chunk chunk;
    private final transient Map<Object, Material> blocksTypes;
    private final transient MeshMaterial meshMaterial;
    private transient Mesh mesh;
    private boolean meshReady = false;

    private float[] verticesArray;
    private int[] indicesArray;
    private float[] uvsArray;
    private float[] normalsArray;

    public ChunkMesh(final Chunk chunk, Map<Object, Material> blocksTypes, MeshMaterial meshMaterial) {
        this.chunk = chunk;
        this.blocksTypes = blocksTypes;
        this.meshMaterial = meshMaterial;
    }

    public void buildMesh() {
        if (chunk.wasModified() || areVBOsArraysEmpty()) {
            System.out.println("It should not be called");
            prepareVAOContent();
        }
        if (!areVBOsArraysEmpty()) {
            try {
                mesh = new Mesh(verticesArray, uvsArray, indicesArray, meshMaterial, Constants.chunkAxisSize);
                meshReady = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanUp() {
        if (mesh != null) {
            mesh.cleanUp();
            meshReady = false;
        }
    }

    public void prepareVAOContent() {
        verticesList = new ArrayList<>();
        indicesList = new ArrayList<>();
        uvsList = new ArrayList<>();
        normalsList = new ArrayList<>();
        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    byte block = chunk.getBlock(x, y, z);
                    if (block != blocksTypes.get("air").getId()) {
                        if (isTopFaceVisible(x, y, z)) {
                            addTopFace(x, y, z, block);
                        }
                        if (isBottomFaceVisible(x, y, z)) {
                            addBottomFace(x, y, z, block);
                        }
                        if (isFrontFaceVisible(x, y, z)) {
                            addFrontFace(x, y, z, block);
                        }
                        if (isBackFaceVisible(x, y, z)) {
                            addBackFace(x, y, z, block);
                        }
                        if (isLeftFaceVisible(x, y, z)) {
                            addLeftFace(x, y, z, block);
                        }
                        if (isRightFaceVisible(x, y, z)) {
                            addRightFace(x,y,z,block);
                        }
                    }
                }
            }
        }
        chunk.setWasModified(false);
        verticesArray = vectorListToArray(verticesList);
        uvsArray = floatListToArray(uvsList);
        normalsArray = vectorListToArray(normalsList);
        indicesArray = intListToArray(indicesList);
        logger.trace("Prepared VBOs arrays");
        logger.trace("Indices: " + indicesArray.length);
        logger.trace("UVs: " + uvsArray.length);
        logger.trace("Normals: " + normalsArray.length);
        logger.trace("Vertices: " + verticesArray.length);
        cleanLists();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public boolean hasMesh() {
        return meshReady;
    }

    private boolean areVBOsArraysEmpty() {
        return verticesArray == null && uvsArray == null && normalsArray == null && indicesArray == null;
    }

    //TODO Strategy?

    private float[] vectorListToArray(List<Vector3f> list) {
        float[] array = new float[list.size() * 3];
        IntStream.range(0, list.size()).forEach(i -> {
            Vector3f vertex = list.get(i);
            array[i*3] = vertex.x;
            array[i*3+1] = vertex.y;
            array[i*3+2] = vertex.z;
        });
        return array;
    }

    private int[] intListToArray(List<Integer> list) {
        int[] array = new int[list.size()];
        IntStream.range(0, list.size()).forEach(i -> array[i] = list.get(i));
        return array;
    }

    private float[] floatListToArray(List<Float> list) {
        float[] array = new float[list.size()];
        IntStream.range(0, list.size()).forEach(i -> array[i] = list.get(i));
        return array;
    }

    private void addFace(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4) {
        int size = verticesList.size() - 1;
        //Add vertices
        verticesList.add(v1);
        verticesList.add(v2);
        verticesList.add(v3);
        verticesList.add(v4);
        //Add indices
        indicesList.add(size + 1);
        indicesList.add(size + 2);
        indicesList.add(size + 4);
        indicesList.add(size + 4);
        indicesList.add(size + 2);
        indicesList.add(size + 3);
    }

    private void addTexture(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4) {
        uvsList.add(v1.x);
        uvsList.add(v1.y);
        uvsList.add(v2.x);
        uvsList.add(v2.y);
        uvsList.add(v3.x);
        uvsList.add(v3.y);
        uvsList.add(v4.x);
        uvsList.add(v4.y);
    }

    private boolean isTopFaceVisible(int x, int y, int z) {
        if (y >= 0 && y < 15) {
            return chunk.getBlock(x, y + 1, z) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    private void addTopFace(float x, float y, float z, byte block) {
        Vector3f v1 = new Vector3f(x, y + 1, z);
        Vector3f v2 = new Vector3f(x, y + 1, z + 1);
        Vector3f v3 = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f v4 = new Vector3f(x + 1, y + 1, z);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(0, 1, 0)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getTopFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    private boolean isBottomFaceVisible(int x, int y, int z) {
        if (y > 0 && y <= 15) {
            if (chunk.getBlock(x, y - 1, z) == blocksTypes.get("air").getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addBottomFace(float x, float y, float z, byte block) {
        Vector3f v1 = new Vector3f(x, y, z + 1);
        Vector3f v2 = new Vector3f(x, y, z);
        Vector3f v3 = new Vector3f(x + 1, y, z);
        Vector3f v4 = new Vector3f(x + 1, y, z + 1);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(0, -1, 0)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getBotFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    private boolean isFrontFaceVisible(int x, int y, int z) {
        if (z < 15) {
            if (chunk.getBlock(x, y, z + 1) == blocksTypes.get("air").getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addFrontFace(float x, float y, float z, byte block) {
        Vector3f v1 = new Vector3f(x, y + 1, z + 1);
        Vector3f v2 = new Vector3f(x, y, z + 1);
        Vector3f v3 = new Vector3f(x + 1, y, z + 1);
        Vector3f v4 = new Vector3f(x + 1, y + 1, z + 1);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(0, 0, 1)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getFrontFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    private boolean isBackFaceVisible(int x, int y, int z) {
        if (z > 0) {
            if (chunk.getBlock(x, y, z - 1) == blocksTypes.get("air").getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addBackFace(float x, float y, float z, byte block) {
        Vector3f v1 = new Vector3f(x + 1, y + 1, z);
        Vector3f v2 = new Vector3f(x + 1, y, z);
        Vector3f v3 = new Vector3f(x, y, z);
        Vector3f v4 = new Vector3f(x, y + 1, z);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(0, 0, -1)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getBackFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    private boolean isLeftFaceVisible(int x, int y, int z) {
        if (x > 0) {
            if (chunk.getBlock(x - 1, y, z) == blocksTypes.get("air").getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addLeftFace(float x, float y, float z, byte block) {
        Vector3f v1 = new Vector3f(x, y + 1, z);
        Vector3f v2 = new Vector3f(x, y, z);
        Vector3f v3 = new Vector3f(x, y, z + 1);
        Vector3f v4 = new Vector3f(x, y + 1, z + 1);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(-1, 0, 0)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getLeftFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    private boolean isRightFaceVisible(int x, int y, int z) {
        if (x < 15) {
            if (chunk.getBlock(x + 1, y, z) == blocksTypes.get("air").getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addRightFace(float x, float y, float z, byte block){
        Vector3f v1 = new Vector3f(x + 1, y + 1, z + 1);
        Vector3f v2 = new Vector3f(x + 1, y, z + 1);
        Vector3f v3 = new Vector3f(x + 1, y, z);
        Vector3f v4 = new Vector3f(x + 1, y + 1, z);
        addFace(v1, v2, v3, v4);
        IntStream.range(0,4).forEach(i -> normalsList.add(new Vector3f(1, 0, 0)));
        BlockTexture.FaceTexture faceTexture = blocksTypes.get(block).getBlockTexture().getRightFace();
        addTexture(faceTexture.getTopLeft(), faceTexture.getBotLeft(), faceTexture.getBotRight(), faceTexture.getTopRight());
    }

    public Chunk getChunk() {
        return chunk;
    }

    private void cleanLists() {
        logger.trace("Cleaning lists");
        this.verticesList = null;
        this.indicesList = null;
        this.uvsList = null;
        this.normalsList = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkMesh chunkMesh = (ChunkMesh) o;
        return chunk.equals(chunkMesh.chunk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunk);
    }
}
