package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.Vao;
import it.cubicworldsimulator.engine.loader.Vbo;

import java.util.List;

public interface Mesh {
    int getVertexCount();
    Vao getVao();
    float getBoundingRadius();
    Material getMeshMaterial();
    List<Vbo> getVboList();
    List<Vbo> getTextureVboList();
}
