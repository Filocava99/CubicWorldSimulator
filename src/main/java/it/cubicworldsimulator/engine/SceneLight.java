package it.cubicworldsimulator.engine;

import org.joml.Vector3f;

import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;

public class SceneLight {

    private Vector3f skyBoxLight;
	private Vector3f ambientLight;
	private PointLight[] pointLights;
	private SpotLight[] spotLights;
	private DirectionalLight directionalLight;
	
	public Vector3f getAmbientLight() {
		return this.ambientLight;
	}
	
	public PointLight[] getPointLights() {
		return this.pointLights;
	}
	
	public SpotLight[] getSpotLights() {
		return this.spotLights;
	}
	
	public DirectionalLight getDirectionalLight() {
		return this.directionalLight;
	}
	
	public void setAmbientLight(Vector3f ambientLight) {
		this.ambientLight = ambientLight;
	}
	
	public void setPointLights(PointLight[] pointLights) {
		this.pointLights = pointLights;
	}
	
	public void setSpotLights(SpotLight[] spotLights) {
		this.spotLights = spotLights;
	}
	
	public void setDirectionalLight(DirectionalLight directionalLight) {
		this.directionalLight = directionalLight;
	}
	
	public Vector3f getSkyBoxLight() {
        return skyBoxLight;
    }

    public void setSkyBoxLight(Vector3f skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }
	
}
