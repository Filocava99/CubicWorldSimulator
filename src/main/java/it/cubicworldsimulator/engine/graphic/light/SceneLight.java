package it.cubicworldsimulator.engine.graphic.light;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.joml.Vector3f;


public class SceneLight {
	
	private Vector3f skyBoxLight;
    private final DirectionalLight directionalLight;
    private final PointLight[] pointLights;
    private final SpotLight[] spotLights;
    private final float specularPower;
    private Vector3f ambientLight;

    public SceneLight(DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights, Vector3f ambientLight, float specularPower) {
        this.directionalLight = directionalLight;
        this.pointLights = pointLights;
        this.spotLights = spotLights;
        this.ambientLight = ambientLight;
        this.specularPower = specularPower;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public float getSpecularPower() {
        return specularPower;
    }
    
    public Vector3f getSkyBoxLight() {
        return skyBoxLight;
    }

    public void setSkyBoxLight(Vector3f skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }
    
    @Override
    public boolean equals(Object o) {
    	if(this == o) return true;
    	if(o == null || getClass() != o.getClass()) return false;
    	SceneLight sl = (SceneLight) o;
    	List<PointLight> point = Arrays.asList(this.pointLights);
    	List<SpotLight> spot = Arrays.asList(this.spotLights);
    	return this.directionalLight.equals(sl.getDirectionalLight()) &&
    			this.ambientLight.equals(sl.getAmbientLight()) &&
    			point.equals(Arrays.asList(sl.getPointLights())) &&
    			spot.equals(Arrays.asList(sl.getSpotLights())) &&
    			this.specularPower == sl.getSpecularPower();
    }
    
    @Override
    public int hashCode() {
    	List<PointLight> point = Arrays.asList(this.pointLights);
    	List<SpotLight> spot = Arrays.asList(this.spotLights);
    	return this.directionalLight.hashCode() 
    			+ point.hashCode() 
    			+ spot.hashCode() 
    			+ this.ambientLight.hashCode();
    }
    
    @Override
	public String toString() {
		return "SceneLight [directionalLight=" + directionalLight + ", pointLights=" + Arrays.toString(pointLights)
				+ ", spotLights=" + Arrays.toString(spotLights) + ", specularPower=" + specularPower + ", ambientLight="
				+ ambientLight + "]";
	}
    
	public static class Builder implements SceneLightBuilder{
    	
    	private DirectionalLight directionalLight;
    	private PointLight[] pointLights = new PointLight[5];
    	private SpotLight[] spotLights = new SpotLight[5];    	
    	private Vector3f ambientLight;
    	private float specularPower;
    	
		@Override
		public SceneLightBuilder addDirectionalLight(DirectionalLight light) throws IllegalStateException {
			Objects.requireNonNull(light);
			if(this.directionalLight != null) {
				throw new IllegalStateException();
			}
			this.directionalLight = light;
			return this;
		}

		@Override
		public SceneLightBuilder addPointLight(PointLight light) {
			Objects.requireNonNull(light);
			this.pointLights[this.pointLights.length] = light;
			return this;
		}

		@Override
		public SceneLightBuilder addSpotLight(SpotLight light) {
			Objects.requireNonNull(light);
			this.spotLights[this.spotLights.length] = light;
			return this;
		}

		@Override
		public SceneLightBuilder addAmbientLight(Vector3f light) throws IllegalStateException {
			Objects.requireNonNull(light);
			if(this.ambientLight != null) {
				throw new IllegalStateException();
			}
			this.ambientLight = light;
			return this;
		}

		@Override
		public SceneLightBuilder addSpecularPower(float specularPower) {
			Objects.requireNonNull(specularPower);
			this.specularPower = specularPower;
			return this;
		}

		@Override
		public SceneLight build() throws IllegalStateException {
			if( this.ambientLight == null || this.directionalLight == null ) {
				throw new IllegalStateException();
			}
			if(this.pointLights[0] == null) {
				this.pointLights = new PointLight[0];
			}
			
			if(this.spotLights[0] == null) {
				this.spotLights = new SpotLight[0];
			}
			
			return new SceneLight(this.directionalLight, this.pointLights, this.spotLights, this.ambientLight, this.specularPower);
		}
    }
}
