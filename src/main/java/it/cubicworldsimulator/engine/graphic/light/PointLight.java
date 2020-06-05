package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public class PointLight {

	private Vector3f color;

	private Vector3f position;

	protected float intensity;

	private Attenuation attenuation;

	public PointLight(Vector3f color, Vector3f position, float intensity) {
		attenuation = new Attenuation(1, 0, 0);
		this.color = color;
		this.position = position;
		this.intensity = intensity;
	}

	public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
		this(color, position, intensity);
		this.attenuation = attenuation;
	}

	public PointLight(PointLight pointLight) {
		this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
				pointLight.getIntensity(), pointLight.getAttenuation());
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
    	if(o == null || getClass() != o.getClass()) return false;
    	PointLight light = (PointLight) o;
    	return this.color.equals(light.color.x, light.color.y, light.color.z) &&
    			this.intensity == light.intensity &&
    			this.position.equals(light.position.x, light.position.y, light.position.z) &&
    			this.attenuation.equals(light.attenuation);
    	
	}
	
	@Override
    public int hashCode() {
    	return this.color.hashCode() 
    			+ this.position.hashCode() 
    			+ this.attenuation.hashCode();
    }

	public static class Attenuation {

		private float constant;

		private float linear;

		private float exponent;

		public Attenuation(float constant, float linear, float exponent) {
			this.constant = constant;
			this.linear = linear;
			this.exponent = exponent;
		}

		public float getConstant() {
			return constant;
		}

		public void setConstant(float constant) {
			this.constant = constant;
		}

		public float getLinear() {
			return linear;
		}

		public void setLinear(float linear) {
			this.linear = linear;
		}

		public float getExponent() {
			return exponent;
		}

		public void setExponent(float exponent) {
			this.exponent = exponent;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
	    	if(o == null || getClass() != o.getClass()) return false;
	    	Attenuation att = (Attenuation) o;
	    	return this.constant == att.constant &&
	    			this.linear == att.linear &&
	    			this.exponent == att.exponent;
	    	
		}
	}
}