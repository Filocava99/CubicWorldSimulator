package it.cubicworldsimulator.engine.graphic.light;

import it.cubicworldsimulator.engine.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

public class DayNightCycleLight implements DayNightCycle {

    private static final Logger logger = LogManager.getLogger(DayNightCycleLight.class);
    private static final float DELTA_INTENSITY = 0.00250f;
    private static final float UPPER_BOUND = 1f;
    private static final float LOWER_BOUND = 0.1f;
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
        time += timer.getElapsedTime()*1000;
        time %= 24_000;
        logger.debug("Time: " + time + "\tBlu: " + light.z);
        if(time >= 0 && time < 5000){
            if ((light.z -= DELTA_INTENSITY) < LOWER_BOUND) {
                light = new Vector3f(light.x, light.y, LOWER_BOUND);
            } else {
                light = new Vector3f(light.x, light.y, light.z+ DELTA_INTENSITY);
            }
        }else if(time >= 5000 && time < 21000){
            if ((light.z += DELTA_INTENSITY) > UPPER_BOUND) {
                light = new Vector3f(light.x, light.y, UPPER_BOUND);
            } else {
                light = new Vector3f(light.x, light.y, light.z+ DELTA_INTENSITY);
            }
        }else {
            if ((light.z -= DELTA_INTENSITY) < LOWER_BOUND) {
                light = new Vector3f(light.x, light.y, LOWER_BOUND);
            } else {
                light = new Vector3f(light.x, light.y, light.z- DELTA_INTENSITY);
            }
        }
        sceneLight.setAmbientLight(light);
    }
}
