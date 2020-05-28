package it.cubicworldsimulator.game.openglcommands;


import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;

public class OpenGLLoadChunkCommand implements OpenGLCommand {

    private final ChunkMesh mesh;

    public OpenGLLoadChunkCommand(ChunkMesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void run() {
        mesh.buildMesh();
    }

    public Mesh[] getMeshes() {
        Mesh[] meshes = new Mesh[2];
        meshes[0] = mesh.getOpaqueMesh();
        meshes[1] = mesh.getTransparentMesh();
        return meshes;
    }
}