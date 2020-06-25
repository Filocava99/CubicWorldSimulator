package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

public interface Observer {
	
	/**
	 * Updates the two parameters, can be called from an Observable
	 * @param position
	 * @param rotation
	 */
	public void update(Vector3f position, Vector3f rotation);
}
