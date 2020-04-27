package it.cubicworldsimulator.game.openglcommands;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OpenGLUnloadChunkCommand implements OpenGLCommand {

    private final ChunkMesh mesh;

    public OpenGLUnloadChunkCommand(ChunkMesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void run() {
        mesh.cleanUp();
    }

    public Mesh[] getMeshes() {
        Mesh[] meshes = new Mesh[2];
        meshes[0] = mesh.getOpaqueMesh();
        meshes[1] = mesh.getTransparentMesh();
        return meshes;
    }
}