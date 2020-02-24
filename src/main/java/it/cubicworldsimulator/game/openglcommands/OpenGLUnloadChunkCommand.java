package it.cubicworldsimulator.game.openglcommands;

import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;

public class OpenGLUnloadChunkCommand implements OpenGLCommand {

    private final ChunkMesh mesh;

    public OpenGLUnloadChunkCommand(ChunkMesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void run() {
        mesh.cleanUp();
    }

    public Mesh getMesh() {
        return mesh.getMesh();
    }
}