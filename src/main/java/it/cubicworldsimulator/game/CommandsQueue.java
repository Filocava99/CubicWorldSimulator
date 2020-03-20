package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.openglcommands.OpenGLCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.utility.BiConcurrentHashMap;
import it.cubicworldsimulator.game.utility.BiMap;
import it.cubicworldsimulator.game.utility.Pair;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandsQueue {

    //TODO Bidirectional map
    private final ConcurrentLinkedQueue<OpenGLLoadChunkCommand> loadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<OpenGLUnloadChunkCommand> unloadCommands = new ConcurrentLinkedQueue<>();
    private final BiMap<Vector3f, OpenGLCommand> loadCommandsMap = new BiConcurrentHashMap<>();
    private final BiMap<Vector3f, OpenGLCommand> unloadCommandsMap = new BiConcurrentHashMap<>();

    public void removeLoadCommand(Vector3f coord){
        OpenGLCommand command = loadCommandsMap.getByKey(coord);
        loadCommands.remove(command);
    }

    public void removeUnloadCommand(Vector3f coord){
        OpenGLCommand command = unloadCommandsMap.getByKey(coord);
        unloadCommands.remove(command);
    }

    public Pair<GameItem, GameItem> runLoadCommand(){
        OpenGLLoadChunkCommand command = loadCommands.poll();
        if(command != null) {
            command.run();
            Vector3f coords = loadCommandsMap.removeByValue(command);
            loadCommandsMap.removeByKey(coords);
            Mesh[] meshes = command.getMeshes();
            if(meshes.length > 0){
                Pair<GameItem, GameItem> pair = new Pair<>();
                Mesh opaqueMesh = meshes[0];
                Mesh transparentMesh = meshes[1];
                if(opaqueMesh != null){
                    GameItem gameItem = new GameItem(command.getMeshes()[0]);
                    gameItem.setPosition((int)coords.x << 4, (int)coords.y << 4, (int)coords.z << 4); //TODO Salvare il 4 come costante nella classe Constants
                    pair.setFirstValue(gameItem);
                }
                if(transparentMesh != null){
                    GameItem gameItem = new GameItem(command.getMeshes()[1]);
                    gameItem.setPosition((int)coords.x << 4, (int)coords.y << 4, (int)coords.z << 4); //TODO Salvare il 4 come costante nella classe Constants
                    pair.setFirstValue(gameItem);
               }
                return pair;
            }
        }
        return null;
    }

    public Pair<GameItem, GameItem> runUnloadCommand(){
        OpenGLUnloadChunkCommand command = unloadCommands.poll();
        if(command != null) {
            command.run();
            Vector3f coords = unloadCommandsMap.removeByValue(command);
            unloadCommandsMap.removeByKey(coords);
            Mesh[] meshes = command.getMeshes();
            if(meshes.length > 0){
                Pair<GameItem, GameItem> pair = new Pair<>();
                Mesh opaqueMesh = meshes[0];
                Mesh transparentMesh = meshes[1];
                if(opaqueMesh != null){
                    GameItem gameItem = new GameItem(command.getMeshes()[0]);
                    gameItem.setPosition((int)coords.x << 4, (int)coords.y << 4, (int)coords.z << 4); //TODO Salvare il 4 come costante nella classe Constants
                    pair.setFirstValue(gameItem);
                }
                if(transparentMesh != null){
                    GameItem gameItem = new GameItem(command.getMeshes()[1]);
                    gameItem.setPosition((int)coords.x << 4, (int)coords.y << 4, (int)coords.z << 4); //TODO Salvare il 4 come costante nella classe Constants
                    pair.setFirstValue(gameItem);
                }
                return pair;
            }
        }
        return null;
    }

    public void addLoadCommand(Vector3f coord, OpenGLLoadChunkCommand command){
        if(unloadCommandsMap.containsKey(coord)){
            unloadCommands.remove(unloadCommandsMap.removeByKey(coord));
        }else{
            loadCommandsMap.put(coord, command);
            loadCommands.add(command);
        }
    }

    public void addUnloadCommand(Vector3f coord, OpenGLUnloadChunkCommand command){
        if(loadCommandsMap.containsKey(coord)){
            loadCommands.remove(loadCommandsMap.removeByKey(coord));
        }else{
            unloadCommandsMap.put(coord, command);
            unloadCommands.add(command);
        }
    }

    public boolean hasLoadCommand(){
        return !loadCommands.isEmpty();
    }

}
