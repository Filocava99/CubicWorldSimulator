package it.cubicworldsimulator.engine.graphic.light;

import it.cubicworldsimulator.engine.Timer;
import it.cubicworldsimulator.engine.hud.GenericHud;
import it.cubicworldsimulator.engine.hud.UpperHud;
import org.joml.Vector3f;

public class DayNightManager implements LightCycleManager {
    private static final float X_UPPER_BOUND = 0.4f;
    private static final float X_LOWER_BOUND = 0.16f;
    private static final float Y_UPPER_BOUND = 0.58f;
    private static final float Y_LOWER_BOUND = 0.16f;
    private static final float Z_UPPER_BOUND = 0.729f;
    private static final float Z_LOWER_BOUND = 0.396f;
    private static final float START_SUNLIGHT = 5f;
    private static final float START_DARKNESS = 21f;
    private static final float MINUTES_PER_HOUR = 60f;
    private static final float HOURS_PER_DAY = 24f;
    private final float dayVelocity;

    private final Timer timer;
    private final SceneLight sceneLight;
    private Vector3f light;
    private float hour = 0f;
    private int minutes = 0;
    private float delta = 1f;
    private final GenericHud upperHud;

    public DayNightManager(final SceneLight sceneLight, final GenericHud upperHud, final float dayVelocity) {
        timer = new Timer();
        this.upperHud = upperHud;
        this.sceneLight = sceneLight;
        this.dayVelocity = dayVelocity;
        getTime();
        light = (hour >= START_SUNLIGHT && hour < START_DARKNESS) ?
                new Vector3f(X_UPPER_BOUND, Y_UPPER_BOUND, Z_UPPER_BOUND):
                new Vector3f(X_LOWER_BOUND, Y_LOWER_BOUND, Z_LOWER_BOUND);
    }

    @Override
    public void setDelta(float delta) {
        this.delta = delta;
    }

    @Override
    public void updateCycle() {
        getTime();
        ((UpperHud) upperHud).setText(String.format("%d:%02d", (int)hour, minutes));
        light = (hour >= START_SUNLIGHT && hour < START_DARKNESS) ? setSunlight() : setDarkness();
        sceneLight.setAmbientLight(light);
    }

    @Override
    public Vector3f setDarkness() {
        return new Vector3f(Math.max(light.x - delta, X_LOWER_BOUND),
                Math.max(light.y - delta, Y_LOWER_BOUND),
                Math.max(light.z - delta, Z_LOWER_BOUND));
    }

    @Override
    public Vector3f setSunlight() {
        return new Vector3f(Math.min(light.x + delta, X_UPPER_BOUND),
                Math.min(light.y + delta, Y_UPPER_BOUND),
                Math.min(light.z + delta, Z_UPPER_BOUND));
    }

    /**
     * It updates hours and minutes.
     */
    private void getTime() {
        hour += ((timer.getElapsedTime() * dayVelocity) % (HOURS_PER_DAY*1000f)) / (1000f);
        int decimals = (int) ((hour - (int) hour) * 100);
        if (decimals >= MINUTES_PER_HOUR) {
            minutes = 0;
            hour = (int) hour == HOURS_PER_DAY - 1 ? 0 : hour + 0.1f;
        } else {
            minutes = decimals;
        }
    }
}
