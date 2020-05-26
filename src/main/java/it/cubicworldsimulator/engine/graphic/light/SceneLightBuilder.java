package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public interface SceneLightBuilder {
	
	public SceneLightBuilder addDirectionalLight(DirectionalLight light);
	public SceneLightBuilder addPointLight(PointLight light);
	public SceneLightBuilder addSpotLight(SpotLight light);
	public SceneLightBuilder addAmbientLight(Vector3f light);
	public SceneLightBuilder addSpecularPower(float specularPower);
	public SceneLight build();
}
