package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public class LightFactoryImpl implements LightFactory {

	@Override
	public PointLight createPointLight(Vector3f color, Vector3f position, float intensity) {
		PointLight.Attenuation attenuation = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
		return new PointLight(color, position, intensity, attenuation);
	}

	@Override
	public DirectionalLight createDirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		return new DirectionalLight(color, direction, intensity);
	}

	@Override
	public SpotLight createSpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
		return new SpotLight(pointLight, coneDirection, cutOffAngle);
	}

	@Override
	public Vector3f createAmbientLight(float red, float green, float blue) {
		return new Vector3f(red, green, blue);
	}

	@Override
	public PointLight duplicatePointLight(PointLight pointLight) {
		return new PointLight(pointLight);
	}

	@Override
	public DirectionalLight duplicateDirectionalLight(DirectionalLight directionalLight) {	
		return new DirectionalLight(directionalLight);
	}

	@Override
	public SpotLight duplicateSpotLight(SpotLight spotLight) {
		return new SpotLight(spotLight);
	}

}
