package it.cubicworldsimulator.graphic.light;

import static org.junit.Assert.assertEquals;

import org.joml.Vector3f;
import org.junit.Test;

import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SceneLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;

public class SceneLightBuilderTest {

	@Test
	public void checkCorrectCreation() {
		SceneLight createdWithBuilder = createWithBuilder();
		SceneLight createdWithoutBuilder = createWithoutBuilder();
		assertEquals(createdWithBuilder.getPointLights().length,0 );
	}
	
	
	private SceneLight createWithBuilder() {
		Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float specularPower = 5f;
        float lightIntensity = 1.0f;
        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);

        return new SceneLight.Builder()
        		.addDirectionalLight(directionalLight)
        		.addAmbientLight(ambientLight)
        		.addSpecularPower(specularPower)
        		.build();
	}
	
	private SceneLight createWithoutBuilder() {
		Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float specularPower = 5f;
        float lightIntensity = 1.0f;
        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        return new SceneLight(directionalLight, new PointLight[0], new SpotLight[0], ambientLight, specularPower);
	}
}
