package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.openglcommands.OpenGLCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.world.chunk.ChunkMesh;
import org.joml.Vector3f;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandsQueue {

    private final ConcurrentLinkedQueue<OpenGLCommand> loadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<OpenGLCommand> unloadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Vector3f, OpenGLCommand> loadCommandsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Vector3f, OpenGLCommand> unloadCommandsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<OpenGLCommand, Vector3f> inverseLoadCommandsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<OpenGLCommand, Vector3f> inverseUnloadCommandsMap = new ConcurrentHashMap<>();

    public void removeLoadCommand(Vector3f coord){
        OpenGLCommand command = loadCommandsMap.get(coord);
        loadCommands.remove(command);
    }

    public void removeUnloadCommand(Vector3f coord){
        OpenGLCommand command = unloadCommandsMap.get(coord);
        unloadCommands.remove(command);
    }

    public GameItem runLoadCommand(){
        OpenGLLoadChunkCommand command = (OpenGLLoadChunkCommand)loadCommands.poll();
        if(command != null) {
            command.run();
            Vector3f coords = inverseLoadCommandsMap.remove(command);
            loadCommandsMap.remove(coords);
            GameItem gameItem = new GameItem(command.getMesh());
            gameItem.setPosition(coords);
            return gameItem;
        }
        return null;
    }

    public GameItem runUnloadCommand(){
        OpenGLUnloadChunkCommand command = (OpenGLUnloadChunkCommand) unloadCommands.poll();
        if(command != null) {
            command.run();
            Vector3f coords = inverseUnloadCommandsMap.remove(command);
            unloadCommandsMap.remove(coords);
            GameItem gameItem = new GameItem(command.getMesh());
            gameItem.setPosition(coords);
            return gameItem;
        }
        return null;
    }

    public void addLoadCommand(Vector3f coord, OpenGLCommand command){
        if(unloadCommandsMap.containsKey(coord)){
            unloadCommands.remove(unloadCommandsMap.remove(coord));
            inverseUnloadCommandsMap.remove(command);
        }else{
            loadCommandsMap.put(coord, command);
            inverseLoadCommandsMap.put(command, coord);
            loadCommands.add(command);
        }
    }

    public void addUnloadCommand(Vector3f coord, OpenGLCommand command){
        if(loadCommandsMap.containsKey(coord)){
            loadCommands.remove(loadCommandsMap.remove(coord));
            inverseLoadCommandsMap.remove(command);
        }else{
            unloadCommandsMap.put(coord, command);
            inverseUnloadCommandsMap.put(command, coord);
            unloadCommands.add(command);
        }
    }

}
