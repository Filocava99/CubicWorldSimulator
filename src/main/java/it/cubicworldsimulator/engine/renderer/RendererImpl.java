package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.GameItem;
import it.cubicworldsimulator.engine.Scene;
import it.cubicworldsimulator.engine.ShaderProgram;
import it.cubicworldsimulator.engine.Transformation;
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

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    public RendererImpl() {
        transformation = new Transformation();
    }

    public void init() {
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Scene scene, float width, float height) {
        clear();

        if (scene != null ) {
            // Update projection Matrix
            Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, width, height, Z_NEAR, Z_FAR);

            if(scene.getSkyBox() != null){
                renderSkyBox(scene.getSkyBox(), projectionMatrix);
            }

            if(scene.getMeshMap() != null){
                scene.getShaderProgram().bind();

                scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
                scene.getShaderProgram().setUniform("texture_sampler", 0);

                // Render each gameItem
                scene.getMeshMap().forEach((k,v) -> {
                    renderList(scene.getShaderProgram(),k,v);
                });
                scene.getShaderProgram().unbind();
            }
        }
    }

    private void render(ShaderProgram shaderProgram, GameItem gameItem){
        initRender(gameItem.getMesh());
        Matrix4f worldMatrix = transformation.getWorldMatrix(gameItem.getPosition(),gameItem.getRotation(),gameItem.getScale());
        shaderProgram.setUniform("worldMatrix", worldMatrix);
        logger.trace("Vertices rendered: " + gameItem.getMesh().getVertexCount());
        glDrawElements(GL_TRIANGLES, gameItem.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
        endRender();
    }

    private void renderSkyBox(SkyBox skyBox, Matrix4f projectionMatrix) {
        skyBox.getShaderProgram().bind();

        skyBox.getShaderProgram().setUniform("texture_sampler", 0);

        // Update projection Matrix
        skyBox.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
        Matrix4f worldMatrix = transformation.getWorldMatrix(skyBox.getPosition(),skyBox.getRotation(),skyBox.getScale());
        worldMatrix.m30(0);
        worldMatrix.m31(0);
        worldMatrix.m32(0);
        skyBox.getShaderProgram().setUniform("worldMatrix", worldMatrix);

        render(skyBox.getShaderProgram(), skyBox);

        skyBox.getShaderProgram().unbind();
    }

    private void renderList(ShaderProgram shaderProgram, Mesh mesh, List<GameItem> gameItems){
        initRender(mesh);
        gameItems.forEach(gameItem -> {
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
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