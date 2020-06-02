package it.cubicworldsimulator.engine.graphic.light;

import it.cubicworldsimulator.engine.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

public class DayNightManager implements LightCycleManager {
    
    private static final float X_UPPER_BOUND = 0.4f;
    private static final float X_LOWER_BOUND = 0.16f;
    private static final float Y_UPPER_BOUND = 0.58f;
    private static final float Y_LOWER_BOUND = 0.16f;
    private static final float Z_UPPER_BOUND = 0.729f;
    private static final float Z_LOWER_BOUND = 0.396f;
    private static final float DAY_DURATION = 24_000;
    private static final float START_SUNLIGHT = 5000;
    private static final float START_DARKNESS = 21_000;

    private final Timer timer;
    private final SceneLight sceneLight;
    private Vector3f light = new Vector3f(0.1f, 0.1f, 0.2f);
    private long time = 0;
    private float delta = 1f;

    public DayNightManager(SceneLight sceneLight) {
        timer = new Timer();
        this.sceneLight = sceneLight;
    }

    @Override
    public void setDelta(float delta) {
        this.delta = delta;
    }

    @Override
    public void updateCycle() {
        time += timer.getElapsedTime()*500;
        time %= DAY_DURATION;
        light = (time >= START_SUNLIGHT && time < START_DARKNESS) ? setSunlight() : setDarkness();
        sceneLight.setAmbientLight(light);
    }

    @Override
    public Vector3f setSunlight() {
        return new Vector3f(Math.max(light.x - delta, X_LOWER_BOUND),
                Math.max(light.y - delta, Y_LOWER_BOUND),
                Math.max(light.z - delta, Z_LOWER_BOUND));
    }

    @Override
    public Vector3f setDarkness() {
        return new Vector3f(Math.min(light.x + delta, X_UPPER_BOUND),
                Math.min(light.y + delta, Y_UPPER_BOUND),
                Math.min(light.z + delta, Z_UPPER_BOUND));
    }
}
