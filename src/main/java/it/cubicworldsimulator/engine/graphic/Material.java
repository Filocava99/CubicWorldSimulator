package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector4f;

public class Material {
	private Vector4f ambientColour;
	private Vector4f diffuseColour;
	private Vector4f specularColour;
	private boolean isTextured;
	private float reflectance;
	
	public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, boolean isTextured, float reflectance){
		this.ambientColour = ambientColour;
		this.diffuseColour = diffuseColour;
		this.specularColour = specularColour;
		this.isTextured = isTextured;
		this.reflectance = reflectance;
	}

	public Vector4f getAmbientColour() {
		return ambientColour;
	}

	public Vector4f getDiffuseColour() {
		return diffuseColour;
	}

	public Vector4f getSpecularColour() {
		return specularColour;
	}

	public boolean isTextured() {
		return isTextured;
	}

	public float getReflectance() {
		return reflectance;
	}

}
