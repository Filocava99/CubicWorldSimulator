package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

@FunctionalInterface
public interface VisualizationStrategy {
	
	public Vector3f calculatePosition(Vector3f position);
}
