package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;

import java.util.List;

public interface Mesh {
    int getVertexCount();
    Vao getVao();
    float getBoundingRadius();
    Material getMeshMaterial();
    List<Vbo> getVboList();
    List<Vbo> getTextureVboList();
}
