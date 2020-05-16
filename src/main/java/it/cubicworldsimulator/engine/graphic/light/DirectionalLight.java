package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;


public class DirectionalLight {

	private float angle;

    private Vector3f color;

    private Vector3f direction;

    private float intensity;

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
        this.angle = 0f;
    }

    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public void changeAngle(float angle){
		if (angle > 90) {
			setIntensity(0);
			if (angle >= 360) {
				angle = -90;
			}
		} else if (angle <= -80 || angle >= 80) {
			float factor = 1 - (float)(Math.abs(angle) - 80)/ 10.0f;
			setIntensity(factor);
			getColor().y = Math.max(factor, 0.9f);
			getColor().z = Math.max(factor, 0.5f);
		} else {
			setIntensity(1);
			getColor().x = 1;
			getColor().y = 1;
			getColor().z = 1;
		}
		double angRad = Math.toRadians(angle);
		getDirection().x = (float) Math.sin(angRad);
		getDirection().y = (float) Math.cos(angRad);
	}

	public float getAngle() {
		return angle;
	}
}
