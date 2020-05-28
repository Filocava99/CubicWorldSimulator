package it.cubicworldsimulator.engine.loader;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.graphic.Mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyMeshBuilder implements MeshBuilder {

    private final Vao myVao;
    private float boundingRadius;
    private Material texture;
    private int indicesLength;
    private final List<Vbo> vboList = new ArrayList<>();
    private final List<Vbo> textureVboList = new ArrayList<>();
    private final List<Vbo> normalsVboList = new ArrayList<>();


    public MyMeshBuilder() {
        myVao = LoaderUtility.createVao();
    }

    @Override
    public MeshBuilder addPositions(float[] positions) {
        Vbo positionVbo = LoaderUtility.createVbo();
        vboList.add(positionVbo);
        LoaderUtility.insertPositionIntoVbo(positionVbo, positions);
        return this;
    }

    @Override
    public MeshBuilder addIndices(int[] indices) {
        Vbo indexVbo = LoaderUtility.createVbo();
        vboList.add(indexVbo);
        LoaderUtility.insertIndicesIntoVbo(indexVbo, indices);
        indicesLength = indices.length;
        return this;
    }

    @Override
    public MeshBuilder addTextCoords(float[] textCoords) {
        Vbo textureVbo = LoaderUtility.createVbo();
        textureVboList.add(textureVbo);
        LoaderUtility.insertTextureIntoVbo(textureVbo, textCoords);
        return this;
    }

    @Override
    public MeshBuilder addNormals(float[] normals) {
        Vbo normalsVbo = LoaderUtility.createVbo();
        normalsVboList.add(normalsVbo);
        LoaderUtility.insertNormalsIntoVbo(normalsVbo, normals);
        return this;
    }

    @Override
    public MeshBuilder addTexture(Material texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public MeshBuilder setBoundingRadius(float value) {
        boundingRadius = value;
        return this;
    }

    @Override
    public Mesh build() {
        if (boundingRadius<0 || indicesLength<0) {
            throw new IllegalStateException();
        }
        return new Mesh(Objects.requireNonNull(texture), boundingRadius, indicesLength,
                Objects.requireNonNull(myVao), Objects.requireNonNull(vboList),
                Objects.requireNonNull(textureVboList));
    }
}
