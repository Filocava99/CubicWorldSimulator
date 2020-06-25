package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.game.openglcommands.OpenGLLoadChunkCommand;
import it.cubicworldsimulator.game.openglcommands.OpenGLUnloadChunkCommand;
import it.cubicworldsimulator.game.utility.BiConcurrentHashMap;
import it.cubicworldsimulator.game.utility.BiMap;
import it.cubicworldsimulator.game.utility.Constants;
import it.cubicworldsimulator.game.utility.Pair;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandsQueue {

    //TODO Schifosamente costoso. Costa O(n) usare le linkedQueue. Serve usare gli hashset. Usare ConcurrentHashMultiSet?
    private final ConcurrentLinkedQueue<OpenGLLoadChunkCommand> loadCommands = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<OpenGLUnloadChunkCommand> unloadCommands = new ConcurrentLinkedQueue<>();
    private final BiMap<Vector3f, OpenGLLoadChunkCommand> loadCommandsMap = new BiConcurrentHashMap<>();
    private final BiMap<Vector3f, OpenGLUnloadChunkCommand> unloadCommandsMap = new BiConcurrentHashMap<>();

    /**
     * Removes a load command using the coordinates as key
     * @param coord coords of the chunk to be loaded
     */
    public void removeLoadCommand(Vector3f coord){
        OpenGLLoadChunkCommand command = loadCommandsMap.getByKey(coord);
        loadCommands.remove(command);
    }

    /**
     * Removes an unload command using the coordinates as key
     * @param coord coords of the chunk to be unloaded
     */
    public void removeUnloadCommand(Vector3f coord){
        OpenGLUnloadChunkCommand command = unloadCommandsMap.getByKey(coord);
        unloadCommands.remove(command);
    }

    /**
     * Execute the openGL command and returns a pair of gameItems
     * @return pair of gameItems that have been loaded in the GPU memory
     */
    public Pair<GameItem, GameItem> runLoadCommand(){
        OpenGLLoadChunkCommand command = loadCommands.poll();
        if(command != null) {
            command.run();
            Optional<Vector3f> optionalCoords = loadCommandsMap.removeByValue(command);
            if(optionalCoords.isPresent()){
                Vector3f coords = optionalCoords.get();
                Mesh[] meshes = command.getMeshes();
                if(meshes.length > 0){
                    Pair<GameItem, GameItem> pair = new Pair<>();
                    Mesh opaqueMesh = meshes[0];
                    Mesh transparentMesh = meshes[1];
                    if(opaqueMesh != null){
                        GameItem gameItem = new GameItem(opaqueMesh);
                        gameItem.setPosition((int)coords.x * Constants.chunkAxisSize, (int)coords.y * Constants.chunkAxisSize, (int)coords.z * Constants.chunkAxisSize);
                        pair.setFirstValue(gameItem);
                    }
                    if(transparentMesh != null){
                        GameItem gameItem = new GameItem(transparentMesh);
                        gameItem.setPosition((int)coords.x * Constants.chunkAxisSize, (int)coords.y * Constants.chunkAxisSize, (int)coords.z * Constants.chunkAxisSize);
                        pair.setSecondValue(gameItem);
                    }
                    return pair;
                }
            }
        }
        return null;
    }

    /**
     * Execute the openGL command and returns a pair of gameItems
     * @return pair of gameItems that have been unloaded from the GPU memory
     */
    public Pair<GameItem, GameItem> runUnloadCommand(){
        OpenGLUnloadChunkCommand command = unloadCommands.poll();
        if(command != null) {
            command.run();
            Optional<Vector3f> optionalCoords = unloadCommandsMap.removeByValue(command);
            if(optionalCoords.isPresent()){
                Vector3f coords = optionalCoords.get();
                Mesh[] meshes = command.getMeshes();
                if(meshes.length > 0){
                    Pair<GameItem, GameItem> pair = new Pair<>();
                    Mesh opaqueMesh = meshes[0];
                    Mesh transparentMesh = meshes[1];
                    if(opaqueMesh != null){
                        GameItem gameItem = new GameItem(opaqueMesh);
                        gameItem.setPosition((int)coords.x << Constants.logBase2ChunkSize, (int)coords.y << Constants.logBase2ChunkSize, (int)coords.z << Constants.logBase2ChunkSize);
                        pair.setFirstValue(gameItem);
                    }
                    if(transparentMesh != null){
                        GameItem gameItem = new GameItem(transparentMesh);
                        gameItem.setPosition((int)coords.x << Constants.logBase2ChunkSize, (int)coords.y << Constants.logBase2ChunkSize, (int)coords.z << Constants.logBase2ChunkSize);
                        pair.setSecondValue(gameItem);
                    }
                    return pair;
                }
            }
        }
        return null;
    }

    /**
     * Adds a load chunk command
     * @param coord coordinates of the chunk to be loaded
     * @param command load command
     */
    public void addLoadCommand(Vector3f coord, OpenGLLoadChunkCommand command){
        if(unloadCommandsMap.containsKey(coord)){
            Optional<OpenGLUnloadChunkCommand> optional = unloadCommandsMap.removeByKey(coord);
            optional.ifPresent(unloadCommands::remove);
        }else{
            loadCommandsMap.put(coord, command);
            loadCommands.add(command);
        }
    }

    /**
     * Adds an uload chunk command
     * @param coord coordinates of the chunk to be unloaded
     * @param command unload command
     */
    public void addUnloadCommand(Vector3f coord, OpenGLUnloadChunkCommand command){
        if(loadCommandsMap.containsKey(coord)){
            Optional<OpenGLLoadChunkCommand> optional = loadCommandsMap.removeByKey(coord);
            optional.ifPresent(loadCommands::remove);
        }else{
            unloadCommandsMap.put(coord, command);
            unloadCommands.add(command);
        }
    }

    /**
     * Checks if a chunk is in the load queue
     * @param coord coordinates of the chunk
     * @return true if the chunk is in the load queue, otherwise false
     */
    public boolean containsLoadCommand(Vector3f coord){
        return loadCommandsMap.containsKey(coord);
    }

    /**
     * Checks if a chunk is in the unload queue
     * @param coord coordinates of the chunk
     * @return true if the chunk is in the unload queue, otherwise false
     */
    public boolean containsUnloadCommand(Vector3f coord){
        return unloadCommandsMap.containsKey(coord);
    }

    /**
     * Checks if the load queue is not empty
     * @return true if the load queue has at least one command
     */
    public boolean hasLoadCommand(){
        return !loadCommands.isEmpty();
    }

}
