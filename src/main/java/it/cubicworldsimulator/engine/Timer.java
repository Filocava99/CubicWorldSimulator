package it.cubicworldsimulator.engine;

public class Timer {

    private double lastLoopTime;

    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Gets time in seconds
     * @return
     */
    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}
