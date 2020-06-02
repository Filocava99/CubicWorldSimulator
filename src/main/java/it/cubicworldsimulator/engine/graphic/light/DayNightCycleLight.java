package it.cubicworldsimulator.engine.graphic.light;

import it.cubicworldsimulator.engine.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

public class DayNightCycleLight implements DayNightCycle {

    private static final Logger logger = LogManager.getLogger(DayNightCycleLight.class);
    private static final float DELTA_INTENSITY = 0.00250f;
    private static final float X_UPPER_BOUND = 0.4f;
    private static final float X_LOWER_BOUND = 0.16f;
    private static final float Y_UPPER_BOUND = 0.58f;
    private static final float Y_LOWER_BOUND = 0.16f;
    private static final float Z_UPPER_BOUND = 0.729f;
    private static final float Z_LOWER_BOUND = 0.396f;
    private final Timer timer;
    private final SceneLight sceneLight;
    private Vector3f light = new Vector3f(0.1f, 0.1f, 0.2f);
    private long time = 0;

    public DayNightCycleLight(SceneLight sceneLight) {
        timer = new Timer();
        this.sceneLight = sceneLight;
    }

    @Override
    public void updateCycle() {
        time += timer.getElapsedTime()*500;
        time %= 24_000;
        //logger.debug("Time: " + time + "\tR: " + light.x + " G: " + light.y + " B: " + light.z);
        if (time >= 0 && time < 5000){
            light = checkLowerBounds(light);
        } else if (time >= 5000 && time < 21000){
            light = checkUpperBounds(light);
        } else {
            light = checkLowerBounds(light);
        }
        sceneLight.setAmbientLight(light);
    }

    private Vector3f checkLowerBounds(Vector3f light) {
        Vector3f newLight = new Vector3f(light.x, light.y, light.z);
        newLight.x = Math.max(light.x - DELTA_INTENSITY, X_LOWER_BOUND);
        newLight.y = Math.max(light.y - DELTA_INTENSITY, Y_LOWER_BOUND);
        newLight.z = Math.max(light.z - DELTA_INTENSITY, Z_LOWER_BOUND);
        return newLight;
    }

    private Vector3f checkUpperBounds(Vector3f light) {
        Vector3f newLight = new Vector3f(light.x, light.y, light.z);
        newLight.x = Math.min(light.x + DELTA_INTENSITY, X_UPPER_BOUND);
        newLight.y = Math.min(light.y + DELTA_INTENSITY, Y_UPPER_BOUND);
        newLight.z = Math.min(light.z + DELTA_INTENSITY, Z_UPPER_BOUND);
        return newLight;
    }
}
