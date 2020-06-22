package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;

import java.util.Set;

public interface Mesh {
    int getVertexCount();
    Vao getVao();
    float getBoundingRadius();
    Material getMeshMaterial();
    Set<Vbo> getVboList();
    Set<Vbo> getTextureVboList();
    void cleanMesh();
}
