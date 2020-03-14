package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.Loader;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private final int vertexCount;
    private final MeshMaterial material;
    private final float boundingRadius;
    private int vaoId;
    private final List<Integer> vboList = new ArrayList<>();
    private final List<Integer> textureVboList = new ArrayList<>();
    private final Loader loader = new Loader();

    public Mesh (float[] positions, float[] textCoords, int[] indices, String textureFileName) {
        this(positions, textCoords, indices, new MeshMaterial(new TextureLoaderImpl().loadTexture(textureFileName)), 0);
    }
/*<<<<<<< HEAD
    
  
    public Mesh(float[] positions, float[] textCoords,float[] normals, int[] indices , MeshMaterial texture, float boundingRadius) {
		FloatBuffer posBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
=======
*/
    public Mesh(float[] positions, float[] textCoords, int[] indices, MeshMaterial texture, float boundingRadius) {
//>>>>>>> cava
        this.material = texture;
        this.boundingRadius = boundingRadius;
        this.vertexCount = indices.length;
        createMesh(positions, textCoords, indices);
    }

    private void createMesh(float[] positions, float[] textCoords, int[] indices) {
       try {
           //Create Vao
           this.vaoId=loader.createVao();

           // Position VBO
           this.vboList.add(loader.createVbo());
           loader.insertPositionIntoVbo(positions, this.vboList.get(vboList.size()-1));

           // Index VBO
           this.vboList.add(loader.createVbo());
           loader.insertIndicesIntoVbo(indices, this.vboList.get(vboList.size()-1));

           //Texture VBO
           this.textureVboList.add(loader.createVbo());
           loader.insertTextureIntoVbo(textCoords, this.textureVboList.get(textureVboList.size()-1));

       } finally {
           loader.cleanBuffers();
       }
   }

    public void cleanUp() {
       loader.cleanVaoAndVbos();
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoId() {
        return this.vaoId;
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public MeshMaterial getMeshMaterial() {
        return material;
    }
}