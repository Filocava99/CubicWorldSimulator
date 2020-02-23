package it.cubicworldsimulator.game;

import it.cubicworldsimulator.game.openglcommands.OpenGLCommand;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;
import org.joml.Vector3f;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandsQueue {

    private final ConcurrentLinkedQueue<OpenGLCommand> loadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<OpenGLCommand> unloadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Vector3f, OpenGLCommand> loadCommandsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Vector3f, OpenGLCommand> unloadCommandsMap = new ConcurrentHashMap<>();

    public void removeLoadCommand(Vector3f coord){
        OpenGLCommand command = loadCommandsMap.get(coord);
        loadCommands.remove(command);
    }

    public void removeUnloadCommand(Vector3f coord){
        OpenGLCommand command = unloadCommandsMap.get(coord);
        unloadCommands.remove(command);
    }

    public void addLoadCommand(Vector3f coord, OpenGLCommand command){
        if(unloadCommands.contains(coord)){
            unloadCommands.remove(unloadCommandsMap.remove(coord));
        }else{
            loadCommandsMap.put(coord, command);
            loadCommands.add(command);
        }
    }

    public void addUnloadCommand(Vector3f coord, OpenGLCommand command){
        if(loadCommandsMap.containsKey(coord)){
            loadCommands.remove(loadCommandsMap.remove(coord));
        }else{
            unloadCommandsMap.put(coord, command);
            unloadCommands.add(command);
        }
    }

}
