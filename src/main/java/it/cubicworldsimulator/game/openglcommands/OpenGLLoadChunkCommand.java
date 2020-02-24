package it.cubicworldsimulator.game.openglcommands;


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
}