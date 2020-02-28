package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.SkyBox;
import it.cubicworldsimulator.engine.graphic.Texture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RendererImpl implements Renderer {

    private static final Logger logger = LogManager.getLogger(RendererImpl.class);

    private final Transformation transformation; //TODO Da discutere

    private final FrustumCullingFilter filter;

    public RendererImpl() {
        transformation = new Transformation();
        filter = new FrustumCullingFilter();
    }

    public void init() {
    }

    /**
     * Clears the screen
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Renders the entire scene (Skybox, Lights, GameItems)
     * @param scene Scene instance
     * @param window Main window instance
     */
    public void render(Scene scene, Window window) {
        //Clear the screen first
        clear();
        //If the scene exists we render it
        if (scene != null) {
            // Update projection Matrix
            Matrix4f projectionMatrix = window.updateProjectionMatrix();
            //If the scene has some GameItems we render them
            if (scene.getMeshMap() != null) {
                //Update the view matrix
                Matrix4f viewMatrix = scene.getCamera().updateViewMatrix();
                //Filter the GameItems based on the frustum
                filter.updateFrustum(projectionMatrix, viewMatrix);
                filter.filter(scene.getMeshMap());
                //Prepare the shader program and the required uniform variables
                scene.getShaderProgram().bind();
                scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
                scene.getShaderProgram().setUniform("texture_sampler", 0);
                // Render each gameItem
                scene.getMeshMap().forEach((k, v) -> {
                    renderListOfGameItems(scene.getShaderProgram(), viewMatrix, k, v);
                });
                //Unbind the shader program
                scene.getShaderProgram().unbind();
            }
            //If the scene has a skybox we render it
            if (scene.getSkyBox() != null) {
                //Renders the skybox
                renderSkyBox(projectionMatrix, scene.getSkyBox(), scene.getCamera());
            }
        }
    }

    /**
     * Renders the skybox
     * @param projectionMatrix
     * @param skyBox
     * @param camera
     */
    private void renderSkyBox(Matrix4f projectionMatrix, SkyBox skyBox, Camera camera) {
        skyBox.getShaderProgram().bind();
        skyBox.getShaderProgram().setUniform("texture_sampler", 0);
        Matrix4f viewMatrix = camera.getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(skyBox, viewMatrix);
        skyBox.getShaderProgram().setUniform("modelViewMatrix", modelViewMatrix);
        renderSingleGameItem(skyBox);
        skyBox.getShaderProgram().unbind();
    }

    /**
     * Renders a single game item
     * @param gameItem GameItem to be rendered
     */
    private void renderSingleGameItem(GameItem gameItem) {
        initRender(gameItem.getMesh());
        logger.trace("GameItem name: " + gameItem.toString());
        logger.trace("Vertices rendered: " + gameItem.getMesh().getVertexCount());
        glDrawElements(GL_TRIANGLES, gameItem.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
        endRender();
    }

    /**
     * Renders a list of game items
     * @param shaderProgram Shader program to be used for the rendering process
     * @param viewMatrix view matrix
     * @param mesh mesh of the game items
     * @param gameItems list of the game items
     */
    private void renderListOfGameItems(ShaderProgram shaderProgram, Matrix4f viewMatrix, Mesh mesh, List<GameItem> gameItems) {
        initRender(mesh);
        gameItems.forEach(gameItem -> {
            if(gameItem.isInsideFrustum()){
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
                shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                logger.trace("GameItem name: " + gameItem.toString());
                logger.trace("Vertices rendered: " + gameItem.getMesh().getVertexCount());
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
        });
        endRender();
    }

    /**
     * Prepares the rendering process
     * @param mesh
     */
    private void initRender(Mesh mesh) {
        Texture texture = mesh.getMeshMaterial().getTexture();
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        //Bind the VAO
        glBindVertexArray(mesh.getVaoId());
    }

    /**
     * Ends the rendering process
     */
    private void endRender() {
        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}