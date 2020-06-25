package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public interface SceneLightBuilder {
	
	/**
	 * Adds a directional light at the scene light
	 * @param light
	 * @return A SceneLightBuilder with the added light
	 */
	public SceneLightBuilder addDirectionalLight(DirectionalLight light);
	
	/**
	 * Adds a point light at the scene light
	 * @param light
	 * @return A SceneLightBuilder with the added light
	 */
	public SceneLightBuilder addPointLight(PointLight light);
	
	/**
	 * Adds a spot light at the scene light
	 * @param light
	 * @return A SceneLightBuilder with the added light
	 */
	public SceneLightBuilder addSpotLight(SpotLight light);
	
	/**
	 * Adds an ambient light at the scene light
	 * @param light
	 * @return A SceneLightBuilder with the added light
	 */
	public SceneLightBuilder addAmbientLight(Vector3f light);
	
	/**
	 * Adds a specular power at the scene light
	 * @param specularPower
	 * @return A SceneLightBuilder with the added specular power
	 */
	public SceneLightBuilder addSpecularPower(float specularPower);
	
	/**
	 * Build a scene light with all the lights added previously
	 * @param light
	 * @return SceneLight
	 */
	public SceneLight build();
}
