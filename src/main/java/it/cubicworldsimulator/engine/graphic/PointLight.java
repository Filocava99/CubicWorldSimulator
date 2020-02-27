package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

public class PointLight {
	
	private Vector3f colour;
	private Vector3f position;
	private float intensity;
	private Attenuation att;
	
	public PointLight(final Vector3f colour, final Vector3f position, final float intensity) {
		this.colour = colour;
		this.position = position;
		this.intensity = intensity;
		this.att = new Attenuation(1,0,0);
	}
	
	public class Attenuation {
		
		private float costant;
		private float linear;
		private float exponent;
		
		public Attenuation(final float costant, final float linear, final float exponent) {
			this.costant = costant;
			this.linear = linear;
			this.exponent = exponent;
		}

		public float getCostant() {
			return costant;
		}

		public float getLinear() {
			return linear;
		}

		public float getExponent() {
			return exponent;
		}
	}

	public Vector3f getColour() {
		return colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getIntensity() {
		return intensity;
	}

	public Attenuation getAtt() {
		return att;
	}

}
