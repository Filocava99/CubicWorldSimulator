package it.cubicworldsimulator.engine.renderer;

import it.cubicworldsimulator.engine.*;
<<<<<<< HEAD
import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.PointLight;
import it.cubicworldsimulator.engine.graphic.SkyBox;
import it.cubicworldsimulator.engine.graphic.SpotLight;
import it.cubicworldsimulator.engine.graphic.Texture;
=======
import it.cubicworldsimulator.engine.graphic.*;
import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;
>>>>>>> master
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RendererImpl implements Renderer {

    private static final Logger logger = LogManager.getLogger(RendererImpl.class);

    private final Transformation transformation;

    private final FrustumCullingFilter filter;

    private final float specularPower = 10f;

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
   /* public void render(Scene scene, Window window) {
        //Clear the screen first
        clear();
        //If the scene exists we render it
        if (scene != null) {
            // Update projection Matrix
            Matrix4f projectionMatrix = window.updateProjectionMatrix();
<<<<<<< HEAD
            
            //If the scene has some GameItems we render them
            if (scene.getOpaqueMeshMap() != null) {
                //Update the view matrix
                Matrix4f viewMatrix = scene.getCamera().updateViewMatrix();

                filter.updateFrustum(projectionMatrix, viewMatrix);
                filter.filter(scene.getOpaqueMeshMap());
                //Prepare the shader program and the required uniform variables
                scene.getShaderProgram().bind();
                scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
                scene.getShaderProgram().setUniform("texture_sampler", 0);
                renderLight(scene, viewMatrix);
=======
            //Update the view matrix
            Matrix4f viewMatrix = scene.getPlayer().updateViewMatrix();


            //Prepare the shader program and the required uniform variables
            scene.getShaderProgram().bind();
            scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
            scene.getShaderProgram().setUniform("texture_sampler", 0);

            SceneLight sceneLight = scene.getSceneLight();
            // Process Point Lights
            int numLights = sceneLight.getPointLights() != null ? sceneLight.getPointLights().length : 0;
            for (int i = 0; i < numLights; i++) {
                // Get a copy of the point light object and transform its position to view coordinates
                PointLight currPointLight = new PointLight(sceneLight.getPointLights()[i]);
                Vector3f lightPos = currPointLight.getPosition();
                Vector4f aux = new Vector4f(lightPos, 1);
                aux.mul(viewMatrix);
                lightPos.x = aux.x;
                lightPos.y = aux.y;
                lightPos.z = aux.z;
                scene.getShaderProgram().setUniform("pointLights", currPointLight, i);
            }

            // Process Spot Ligths
            numLights = sceneLight.getSpotLights() != null ? sceneLight.getSpotLights().length : 0;
            for (int i = 0; i < numLights; i++) {
                // Get a copy of the spot light object and transform its position and cone direction to view coordinates
                SpotLight currSpotLight = new SpotLight(sceneLight.getSpotLights()[i]);
                Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
                dir.mul(viewMatrix);
                currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
                Vector3f lightPos = currSpotLight.getPointLight().getPosition();

                Vector4f aux = new Vector4f(lightPos, 1);
                aux.mul(viewMatrix);
                lightPos.x = aux.x;
                lightPos.y = aux.y;
                lightPos.z = aux.z;

                scene.getShaderProgram().setUniform("spotLights", currSpotLight, i);
            }
            // Update Light Uniforms
            scene.getShaderProgram().setUniform("ambientLight", scene.getSceneLight().getAmbientLight());
            scene.getShaderProgram().setUniform("specularPower", scene.getSceneLight().getSpecularPower());
            DirectionalLight currDirLight = new DirectionalLight(scene.getSceneLight().getDirectionalLight());
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            scene.getShaderProgram().setUniform("directionalLight", currDirLight);




            //Update frustum culling
            filter.updateFrustum(projectionMatrix, viewMatrix);
            //If the scene has some GameItems we render them
            if (scene.getOpaqueMeshMap() != null) {
                //Filter the GameItems based on the frustum
                filter.filter(scene.getOpaqueMeshMap());
>>>>>>> master
                // Render each gameItem
                scene.getOpaqueMeshMap().forEach((k, v) -> {
                    renderListOfGameItems(scene.getShaderProgram(), viewMatrix, k, v);
                });
<<<<<<< HEAD

                //Unbind the shader program

                scene.getShaderProgram().unbind();
=======
>>>>>>> master
            }
            if(scene.getTransparentMeshMap() != null){
                filter.filter(scene.getTransparentMeshMap());
<<<<<<< HEAD
                //Prepare the shader program and the required uniform variables
                scene.getShaderProgram().bind();
                scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
                scene.getShaderProgram().setUniform("texture_sampler", 0);
                renderLight(scene, viewMatrix);
                //glDisable(GL_DEPTH_TEST);
=======
>>>>>>> master
                // Render each gameItem
                scene.getTransparentMeshMap().forEach((k, v) -> {
                    renderListOfGameItems(scene.getShaderProgram(), viewMatrix, k, v);
                });
            }
            //Unbind the shader program
            scene.getShaderProgram().unbind();
            //If the scene has a skybox we render it
            if (scene.getSkyBox() != null) {
                //Renders the skybox
                renderSkyBox(projectionMatrix, scene.getSkyBox(), scene.getPlayer());
            }
        }
    }
    */
    public void render(Scene scene, Window window) {
        //Clear the screen first
        clear();
        //If the scene exists we render it
        if (scene != null) {
            // Update projection Matrix
            Matrix4f projectionMatrix = window.updateProjectionMatrix();
            //Update the view matrix
            Matrix4f viewMatrix = scene.getCamera().updateViewMatrix();
           
            
            //Prepare the shader program and the required uniform variables
            scene.getShaderProgram().bind();
            scene.getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
            scene.getShaderProgram().setUniform("texture_sampler", 0);
            
            renderLight(scene, viewMatrix);
            //Update frustum culling
            filter.updateFrustum(projectionMatrix, viewMatrix);
            //If the scene has some GameItems we render them
            if (scene.getOpaqueMeshMap() != null) {
                //Filter the GameItems based on the frustum
                filter.filter(scene.getOpaqueMeshMap());
                // Render each gameItem
                scene.getOpaqueMeshMap().forEach((k, v) -> {
                    renderListOfGameItems(scene.getShaderProgram(), viewMatrix, k, v);
                });
            }
            if(scene.getTransparentMeshMap() != null){
                filter.filter(scene.getTransparentMeshMap());
                //glDisable(GL_DEPTH_TEST);d
                // Render each gameItem
                scene.getTransparentMeshMap().forEach((k, v) -> {
                    renderListOfGameItems(scene.getShaderProgram(), viewMatrix, k, v);
                });
                //glEnable(GL_DEPTH_TEST);
            }
            //Unbind the shader program
            scene.getShaderProgram().unbind();
            //If the scene has a skybox we render it
            
            //DECOMMENTARE
           // if (scene.getSkyBox() != null) {
                //Renders the skybox
             //   renderSkyBox(projectionMatrix, scene.getSkyBox(), scene.getCamera());
            //}
        }
    }

    
    private void renderLight(Scene scene, Matrix4f viewMatrix) {
    	
    	SceneLight sceneLight = scene.getSceneLight();
    	
    	scene.getShaderProgram().setUniform("ambientLight", sceneLight.getAmbientLight());
    	scene.getShaderProgram().setUniform("specularPower", specularPower);
    	
    	//PointLights
    	PointLight[] pointLightList = sceneLight.getPointLights();
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
    	SpotLight[] spotLightList = sceneLight.getSpotLights();
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
    	DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
    	Vector4f dir = new Vector4f(currDirLight.getDirection(),0);
    	dir.mul(viewMatrix);
    	currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
    	scene.getShaderProgram().setUniform("directionalLight", currDirLight);
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
        shaderProgram.setUniform("material", mesh.getMeshMaterial());
        gameItems.forEach(gameItem -> {
            if(gameItem.isInsideFrustum()){
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
                shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                shaderProgram.setUniform("material",mesh.getMeshMaterial());
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

    private void prepareFrustumCulling(Matrix4f projectionMatrix, Matrix4f viewMatrix, Map<Mesh, List<GameItem>> meshMap){
        //Filter the GameItems based on the frustum
        filter.updateFrustum(projectionMatrix, viewMatrix);
        filter.filter(meshMap);
    }
}