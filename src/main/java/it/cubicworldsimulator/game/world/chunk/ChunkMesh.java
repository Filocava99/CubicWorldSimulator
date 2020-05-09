package it.cubicworldsimulator.game.world.chunk;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.loader.Loader;
import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.world.block.BlockTexture;
import it.cubicworldsimulator.game.world.block.BlockMaterial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class ChunkMesh implements Serializable {
    private static transient final Logger logger = LogManager.getLogger(ChunkMesh.class);

    private final transient Chunk chunk;
    private final transient Map<Object, BlockMaterial> blocksTypes;

    private VBOContainer opaqueMesh;
    private VBOContainer transparentMesh;
    private transient boolean meshesReady = false;

    public ChunkMesh(final Chunk chunk, Map<Object, BlockMaterial> blocksTypes, Material material) {
        this.chunk = chunk;
        this.blocksTypes = blocksTypes;
        this.opaqueMesh = new VBOContainer(material, blocksTypes);
        this.transparentMesh = new VBOContainer(material, blocksTypes);
    }

    /**
     * Initialize the mesh instance. That method must be called from the main thread otherwise OpenGL will trigger an exception
     * and the game will crash
     */
    public void buildMesh() {
        if (chunk.wasModified()) {
            prepareVAOContent();
        }
        try {
            opaqueMesh.buildMesh();
            transparentMesh.buildMesh();
            meshesReady = true;
        } catch (Exception e) {

        }
    }

    /**
     * Delete the meshes instances and its VBOs/VAOs
     */
    public void cleanUp() {
        opaqueMesh.cleanUp();
        transparentMesh.cleanUp();
        meshesReady = false;
    }

    /**
     * Prepare the arrays to be used to instantiate the mesh. It is computationally expensive so it is suggested to call it
     * from a separate thread. It is OpenGL thread safe.
     */
    public void prepareVAOContent() {
        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    byte block = chunk.getBlock(x, y, z);
                    if (block != blocksTypes.get("air").getId()) {
                        if (isTopFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addTopFace(x, y, z, block, transparentMesh);
                            }else{
                                addTopFace(x, y, z, block, opaqueMesh);
                            }
                        }
                        if (isBottomFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addBottomFace(x, y, z, block, transparentMesh);
                            }else{
                                addBottomFace(x, y, z, block, opaqueMesh);
                            }
                        }
                        if (isFrontFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addFrontFace(x, y, z, block, transparentMesh);
                            }else{
                                addFrontFace(x, y, z, block, opaqueMesh);
                            }
                        }
                        if (isBackFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addBackFace(x, y, z, block, transparentMesh);
                            }else{
                                addBackFace(x, y, z, block, opaqueMesh);
                            }
                        }
                        if (isLeftFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addLeftFace(x, y, z, block, transparentMesh);
                            }else{
                                addLeftFace(x, y, z, block, opaqueMesh);
                            }
                        }
                        if (isRightFaceVisible(x, y, z)) {
                            if(blocksTypes.get(block).isTransparent()){
                                addRightFace(x, y, z, block, transparentMesh);
                            }else{
                                addRightFace(x, y, z, block, opaqueMesh);
                            }
                        }
                    }
                }
            }
        }
        opaqueMesh.prepareVAOContent();
        transparentMesh.prepareVAOContent();
        chunk.setWasModified(false);
    }

    /**
     * Return the chunk transparent mesh
     * @return Mesh
     */
    public Mesh getTransparentMesh() {
        return transparentMesh.getMesh();
    }

    /**
     * Return the chunk opaque mesh
     * @return Mesh
     */
    public Mesh getOpaqueMesh() {
        return opaqueMesh.getMesh();
    }

    /**
     * Returns true if the meshes can be used (already loaded on the GPU), otherwise returns false
     * @return boolean
     */
    public boolean hasMeshes() {
        return meshesReady;
    }

    /**
     * Returns true if the top face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isTopFaceVisible(int x, int y, int z) {
        if (y >= 0 && y < 15) {
            return chunk.getBlock(x, y + 1, z) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the top face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addTopFace(float x, float y, float z, byte block, VBOContainer vboContainer) {
        vboContainer.addTopFace(x,y,z,block);
    }

    /**
     * Returns true if the bottom face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isBottomFaceVisible(int x, int y, int z) {
        if (y > 0 && y <= 15) {
            return chunk.getBlock(x, y - 1, z) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the bottom face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addBottomFace(float x, float y, float z, byte block, VBOContainer vboContainer) {
        vboContainer.addBottomFace(x,y,z,block);
    }

    /**
     * Returns true if the front face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isFrontFaceVisible(int x, int y, int z) {
        if (z < 15) {
            return chunk.getBlock(x, y, z + 1) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the front face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addFrontFace(float x, float y, float z, byte block, VBOContainer vboContainer) {
        vboContainer.addFrontFace(x,y,z,block);
    }

    /**
     * Returns true if the back face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isBackFaceVisible(int x, int y, int z) {
        if (z > 0) {
            return chunk.getBlock(x, y, z - 1) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the back face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addBackFace(float x, float y, float z, byte block, VBOContainer vboContainer) {
        vboContainer.addBackFace(x, y, z, block);
    }

    /**
     * Returns true if the left face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isLeftFaceVisible(int x, int y, int z) {
        if (x > 0) {
            return chunk.getBlock(x - 1, y, z) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the left face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addLeftFace(float x, float y, float z, byte block, VBOContainer vboContainer) {
        vboContainer.addLeftFace(x,y,z,block);
    }

    /**
     * Returns true if the right face of the cube is not hidden by another block
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @return Boolean
     */
    private boolean isRightFaceVisible(int x, int y, int z) {
        if (x < 15) {
            return chunk.getBlock(x + 1, y, z) == blocksTypes.get("air").getId();
        } else {
            return true;
        }
    }

    /**
     * Adds all the elements required to render the right face of a cube(vertices, indices, normals and textures coordinates)
     * @param x block x coordinate
     * @param y block y coordinate
     * @param z block z coordinate
     * @param block block id
     * @param vboContainer the VBOContainer that will hold the elements
     */
    private void addRightFace(float x, float y, float z, byte block, VBOContainer vboContainer){
        vboContainer.addRightFace(x, y, z, block);
    }

    /**
     * Returns the chunk on which it is based the mesh
     * @return Chunk
     */
    public Chunk getChunk() {
        return chunk;
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

    private static class VBOContainer implements Serializable{
        private float[] verticesArray;
        private int[] indicesArray;
        private float[] uvsArray;
        private float[] normalsArray;

        private transient List<Vector3f> verticesList;
        private transient List<Integer> indicesList;
        private transient List<Float> uvsList;
        private transient List<Vector3f> normalsList;

        private transient Mesh mesh;
        private transient boolean meshReady;

        private final transient Material material;
        private final transient Map<Object, BlockMaterial> blocksTypes;

        public VBOContainer(Material material, Map<Object, BlockMaterial> blocksTypes) {
            this.material = material;
            this.blocksTypes = blocksTypes;
            this.verticesList = new ArrayList<>();
            this.indicesList = new ArrayList<>();
            this.uvsList = new ArrayList<>();
            this.normalsList = new ArrayList<>();
        }

        /**
         * Initialize the mesh instance. That method must be called from the main thread otherwise OpenGL will trigger an exception, and
         *  the game will crash.
         */
        public void buildMesh() {
            if (areVBOsArraysEmpty()) {
                System.out.println("It should not be called");
                prepareVAOContent();
            }
            if (!areVBOsArraysEmpty()) {
                try {
                    mesh = Loader.createMesh(verticesArray, uvsArray, indicesArray, normalsArray, material,     Constants.chunkAxisSize);
                    meshReady = true;
                } catch (Exception e) {

                }
            }
        }

        /**
         * Delete the mesh instance and its VBOs/VAOs
         */
        public void cleanUp() {
            if (mesh != null) {
                Loader.cleanMesh(mesh);
                meshReady = false;
            }
        }

        /**
         * Prepare the arrays to be used to instantiate the mesh. It is computationally expensive, so it is suggested to call it
         * from a separate thread. It is OpenGL thread safe.
         */
        public void prepareVAOContent() {
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

        /**
         * Return the chunk mesh
         * @return Mesh
         */
        public Mesh getMesh() {
            return mesh;
        }

        /**
         * Returns true if the mesh can be used (already loaded on the GPU), otherwise returns false
         * @return boolean
         */
        public boolean hasMesh() {
            return meshReady;
        }

        /**
         * Returns true if the arrays used to instantiate the mesh are empty
         * @return Boolean
         */
        private boolean areVBOsArraysEmpty() {
            return verticesArray == null && uvsArray == null && normalsArray == null && indicesArray == null;
        }

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

        /**
         * Adds the vertices and indices of a face of a cube
         * @param v1 Top left coordinate of the face
         * @param v2 Bottom left coordinate of the face
         * @param v3 Bottom right coordinate of the face
         * @param v4 Top right coordinate of the face
         */
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

        /**
         * Adds the texture coordinates of a face of a cube
         * @param v1 Top left coordinate of the texture
         * @param v2 Bottom left coordinate of the texture
         * @param v3 Bottom right coordinate of the texture
         * @param v4 Top right coordinate of the texture
         */
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

        /**
         * Adds all the elements required to render the top face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Adds all the elements required to render the bottom face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Adds all the elements required to render the front face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Adds all the elements required to render the back face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Adds all the elements required to render the left face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Adds all the elements required to render the right face of a cube(vertices, indices, normals and textures coordinates)
         * @param x block x coordinate
         * @param y block y coordinate
         * @param z block z coordinate
         * @param block block id
         */
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

        /**
         * Deletes all the lists used to build the VBOs arrays
         */
        private void cleanLists() {
            logger.trace("Cleaning lists");
            this.verticesList = null;
            this.indicesList = null;
            this.uvsList = null;
            this.normalsList = null;
        }
    }
}
