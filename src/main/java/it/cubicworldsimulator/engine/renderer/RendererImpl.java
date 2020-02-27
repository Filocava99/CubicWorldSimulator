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

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Scene scene, Window window) {
        clear();
        if (scene != null) {
            // Update projection Matrix
            Matrix4f projectionMatrix = window.updateProjectionMatrix();
            if (scene.getMeshMap() != null) {
                Matrix4f viewMatrix = scene.getCamera().updateViewMatrix();
                filter.updateFrustum(projectionMatrix, viewMatrix);
                filter.filter(scene.getMeshMap());

                scene.getShaderProgram().bind();
                scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
                scene.getShaderProgram().setUniform("texture_sampler", 0);

                // Render each gameItem
                scene.getMeshMap().forEach((k, v) -> {
                    renderList(scene.getShaderProgram(), viewMatrix, k, v);
                });
                scene.getShaderProgram().unbind();
            }
            if (scene.getSkyBox() != null) {
                renderSkyBox(projectionMatrix, scene.getSkyBox(), scene.getCamera());
            }
        }
    }

    private void renderSkyBox(Matrix4f projectionMatrix, SkyBox skyBox, Camera camera) {
        skyBox.getShaderProgram().bind();
        skyBox.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
        skyBox.getShaderProgram().setUniform("texture_sampler", 0);
        Matrix4f viewMatrix = camera.getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(skyBox, viewMatrix);
        skyBox.getShaderProgram().setUniform("modelViewMatrix", modelViewMatrix);
        render(skyBox);
        skyBox.getShaderProgram().unbind();
    }

    private void render(GameItem gameItem) {
        initRender(gameItem.getMesh());
        logger.trace("GameItem name: " + gameItem.toString());
        logger.trace("Vertices rendered: " + gameItem.getMesh().getVertexCount());
        glDrawElements(GL_TRIANGLES, gameItem.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
        endRender();
    }

    private void renderList(ShaderProgram shaderProgram, Matrix4f viewMatrix, Mesh mesh, List<GameItem> gameItems) {
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

    private void endRender() {
        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}