package it.cubicworldsimulator.game.openglcommands;


import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;

import java.util.List;

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
        return List.of(mesh.getOpaqueMesh(), mesh.getTransparentMesh()).toArray(Mesh[]::new);
    }
}