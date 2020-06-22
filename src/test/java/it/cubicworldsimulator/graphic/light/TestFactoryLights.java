package it.cubicworldsimulator.graphic.light;

import static org.junit.Assert.assertEquals;

import org.joml.Vector3f;
import org.junit.Test;

import it.cubicworldsimulator.engine.graphic.light.DirectionalLight;
import it.cubicworldsimulator.engine.graphic.light.LightFactory;
import it.cubicworldsimulator.engine.graphic.light.LightFactoryImpl;
import it.cubicworldsimulator.engine.graphic.light.PointLight;
import it.cubicworldsimulator.engine.graphic.light.SpotLight;

public class TestFactoryLights {

    LightFactory lightFactory = new LightFactoryImpl();
    
	@Test
	public void checkDirectionalLight() {
	
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(-1, 0, 0), 1.0f);
		DirectionalLight directionalLight2 = lightFactory.createDirectionalLight(new Vector3f(1, 1, 1), new Vector3f(-1, 0, 0), 1.0f);
		DirectionalLight directionalLight3 = lightFactory.duplicateDirectionalLight(directionalLight);
		
		assertEquals(directionalLight, directionalLight2);
		assertEquals(directionalLight, directionalLight3);
		
	}
	
	
	@Test
	public void checkPointLight() {
	
		PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), new Vector3f(-1, 0, 0), 1.0f);
		PointLight pointLight2 = lightFactory.createPointLight(new Vector3f(1, 1, 1), new Vector3f(-1, 0, 0), 1.0f);
		PointLight pointLight3 = lightFactory.duplicatePointLight(pointLight);
		
		assertEquals(pointLight, pointLight2);
		assertEquals(pointLight, pointLight3);
		
	}
	
	@Test
	public void checkSpotLight() {
	
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), new Vector3f(-1, 0, 0), 1.0f);
		SpotLight spotLight = new SpotLight(pointLight, new Vector3f(-1, 0, 0), 1.0f);
		SpotLight spotLight2 = lightFactory.createSpotLight(pointLight, new Vector3f(-1, 0, 0), 1.0f);
		SpotLight spotLight3 = lightFactory.duplicateSpotLight(spotLight);
		
		assertEquals(spotLight, spotLight2);
		assertEquals(spotLight, spotLight3);
		
	}

} 

