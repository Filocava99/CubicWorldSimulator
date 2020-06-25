package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vao;
import it.cubicworldsimulator.engine.loader.OpenGLComponent.Vbo;

import java.util.Set;

/**
 * It models a mesh (also called raw model)
 */
public interface Mesh {
    /**
     * @return teh vertex count of the mesh
     */
    int getVertexCount();

    /**
     * @return the main VAO
     */
    Vao getVao();

    /**
     * @return the boundingRadius mesh value
     */
    float getBoundingRadius();

    /**
     * @return the mesh material
     */
    Material getMeshMaterial();

    /**
     * @return entire vbo list
     */
    Set<Vbo> getVboList();

    /**
     * @return only texture vbo list
     */
    Set<Vbo> getTextureVboList();

    /**
     * Clean mesh when need to clean memory
     */
    void cleanMesh();
}
