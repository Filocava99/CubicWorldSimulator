package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public interface LightFactory {
	
	/**
	 * Creates a new point light that represent the light emitted from a lamp
	 * @param color
	 * @param position
	 * @param intensity
	 * @return The new point light
	 */
	public PointLight createPointLight(Vector3f color, Vector3f position, float intensity);
	
	/**
	 * Creates a new directional light that represent the light emitted from the sun 
	 * @param color
	 * @param direction
	 * @param intensity
	 * @return The new directional light
	 */
	public DirectionalLight createDirectionalLight(Vector3f color, Vector3f direction, float intensity);
	
	/**
	 * Creates a new spot light that represent the light emitted from a lamp but restricted at a cone
	 * @param pointLight
	 * @param coneDirection
	 * @param cutOffAngle
	 * @return The new spot light
	 */
	public SpotLight createSpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle);
	
	/**
	 * Creates a new ambient light that represent a light emitted from any direction
	 * @param red
	 * @param green
	 * @param blue
	 * @return 3D vector that represent light color 
	 */
	public Vector3f createAmbientLight(float red, float green, float blue);
	
	/**
	 * Duplicate a point light from an existing one
	 * @param pointLight
	 * @return The new point light
	 */
	public PointLight duplicatePointLight(PointLight pointLight);
	
	/**
	 *  Duplicate a directional light from an existing one
	 * @param directionalLight
	 * @return The new directional light
	 */
	public DirectionalLight duplicateDirectionalLight(DirectionalLight directionalLight);
	
	/**
	 *  Duplicate a spot light from an existing one
	 * @param spotLight
	 * @return The new spot light
	 */
	public SpotLight duplicateSpotLight(SpotLight spotLight);
	
}
