package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

public class SpotLight {
	private PointLight pointLight;
	private Vector3f coneDirection;
	private float cutoffAngleCosine;
	
	public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutoffAngleCosine) {
		this.pointLight = pointLight;
		this.coneDirection = coneDirection;
		this.cutoffAngleCosine = cutoffAngleCosine;
	}
	
	public PointLight getPointLight() {
		return this.pointLight;
	}
	
	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}
	
	public Vector3f getConeDirection() {
		return this.coneDirection;
	}
	
	public void setConeDirection(Vector3f coneDirection) {
		this.coneDirection = coneDirection;
	}
	
	public float getCutoffAngleCosine() {
		return this.cutoffAngleCosine;
	}
	//TODO forse non serve
	public void setCutoffAngleCosine(float cutoffAngleCosine) {
		this.cutoffAngleCosine = cutoffAngleCosine;
	}
	
	public float getCutoffAngle() {
		return (float) Math.acos(this.cutoffAngleCosine);
	}
	
	public void setCutoffAngle(float cutoffAngle) {
		this.setCutoffAngleCosine((float) Math.cos(Math.toRadians(cutoffAngle)));
	}
	
}
