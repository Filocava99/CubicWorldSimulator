package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public interface LightFactory {
	
	public PointLight createPointLight(Vector3f color, Vector3f position, float intensity);
	public DirectionalLight createDirectionalLight(Vector3f color, Vector3f direction, float intensity);
	public SpotLight createSpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle);
	public Vector3f createAmbientLight(float red, float green, float blue);
	
	public PointLight duplicatePointLight(PointLight pointLight);
	public DirectionalLight duplicateDirectionalLight(DirectionalLight directionalLight);
	public SpotLight duplicateSpotLight(SpotLight spotLight);
	
}
