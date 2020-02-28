package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.*;
import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.PointLight;
import it.cubicworldsimulator.engine.graphic.SkyBox;
import it.cubicworldsimulator.engine.graphic.SpotLight;
import it.cubicworldsimulator.engine.graphic.Texture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RendererImpl implements Renderer {

    private static final Logger logger = LogManager.getLogger(RendererImpl.class);

    private final Transformation transformation; //TODO Da discutere
    private final FrustumCullingFilter filter;
    private final float specularPower;

    public RendererImpl() {
        transformation = new Transformation();
        filter = new FrustumCullingFilter();
        this.specularPower = 10f;
    }

    public void init() {
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Scene scene, Window window, Vector3f ambientLight, PointLight[] pointLightList,
    		SpotLight[] spotLightList, DirectionalLight directionalLight) {
        clear();
        if (scene != null) {
            // Update projection Matrix
            Matrix4f projectionMatrix = window.updateProjectionMatrix();
            if (scene.getMeshMap() != null) {
                Matrix4f viewMatrix = scene.getCamera().updateViewMatrix();
                renderLight(scene, viewMatrix, ambientLight, pointLightList, spotLightList, directionalLight);
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
                shaderProgram.setUniform("material", mesh.getMeshMaterial());
            }
        });
        endRender();
    }
    
    private void renderLight(Scene scene, Matrix4f viewMatrix, Vector3f ambientLight, PointLight[] pointLightList, 
    		SpotLight[] spotLightList, DirectionalLight directionalLight) {
    	
    	scene.getShaderProgram().setUniform("ambientLight", ambientLight);
    	scene.getShaderProgram().setUniform("specularPower", specularPower);
    	
    	//PointLights
    	int numPointLights = pointLightList != null ? pointLightList.length : 0;
    	for (int i = 0; i < numPointLights; i++) {
    		PointLight currPointLight = new PointLight(pointLightList[i]);
    		Vector3f lightPos = currPointLight.getPosition();
    		Vector4f aux = new Vector4f(lightPos, 1);
    		aux.mul(viewMatrix);
    		lightPos.x = aux.x;
    		lightPos.y = aux.y;
    		lightPos.z = aux.z;
    		scene.getShaderProgram().setUniform("pointLights", currPointLight, i);
    	}
    	
    	//SpotLight
    	int numSpotLights = spotLightList != null ? spotLightList.length : 0;
    	for (int i = 0; i < numSpotLights; i++) {
    		SpotLight currSpotLight = new SpotLight(spotLightList[i]);
    		Vector3f lightPos = currSpotLight.getConeDirection();
    		Vector4f aux = new Vector4f(lightPos, 1);
    		aux.mul(viewMatrix);
    		lightPos.x = aux.x;
    		lightPos.y = aux.y;
    		lightPos.z = aux.z;
    		scene.getShaderProgram().setUniform("spotLights", currSpotLight, i);
    	}
    	
    	//DirectionalLight
    	DirectionalLight currDirLight = new DirectionalLight(directionalLight);
    	Vector4f dir = new Vector4f(currDirLight.getDirection(),0);
    	dir.mul(viewMatrix);
    	currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
    	scene.getShaderProgram().setUniform("directionalLight", currDirLight);
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