package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

@FunctionalInterface
public interface VisualizationStrategy {
	
	/**
	 * Calculates the new position starting from old position and rotation
	 * @param position Old position
	 * @param rotation Old rotation
	 * @return
	 */
	public Vector3f calculatePosition(Vector3f position, Vector3f rotation);
}
