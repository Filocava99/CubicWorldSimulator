package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public interface LightCycleManager {
    /**
     * set delta of light intensity increment or decrement
     */
    void setDelta(float delta);

    /**
     * It manages the intensity of light based on time
     */
    void updateCycle();

    /**
     * @return vector with a new larger light intensity
     */
    Vector3f setSunlight();

    /**
     * @return vector with a new smaller light intensity
     */
    Vector3f setDarkness();
}
