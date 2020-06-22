package it.cubicworldsimulator.game.gui;

import java.util.Optional;

/**
 * @author Lorenzo Balzani
 */
public class Settings {
    private final boolean vSync;
    private final boolean debug;
    private final boolean fullscreen;
    private final int width;
    private final int height;
    private final int renderingDistance;
    private final long worldSeed;
    private final String worldName;
    private final float daySpeed;

    public Settings(final boolean vSync, final boolean debug, final boolean fullscreen,
                    final int width, final int height, final int renderingDistance,
                    final long worldSeed, final String worldName, final float daySpeed) {
        this.vSync = vSync;
        this.debug = debug;
        this.fullscreen = fullscreen;
        this.width = width;
        this.height = height;
        this.renderingDistance = renderingDistance;
        this.worldSeed = worldSeed;
        this.worldName = worldName;
        this.daySpeed = daySpeed;
    }

    public boolean getvSync() {
        return vSync;
    }

    public boolean getDebug() {
        return debug;
    }

    public boolean getFullscreen() {
        return fullscreen;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRenderingDistance() {
        return renderingDistance;
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public String getWorldName() {
        return worldName;
    }

    public float getDaySpeed() {
        return daySpeed;
    }

    /**
     * Build a settings object by using builder design pattern.
     */
    public static class Builder {
        private Optional<Boolean> vSync = Optional.empty();
        private Optional<Boolean> debug = Optional.empty();
        private Optional<Boolean> fullscreen = Optional.empty();
        private Optional<Integer> width = Optional.empty();
        private Optional<Integer> height = Optional.empty();
        private Optional<Integer> renderingDistance = Optional.empty();
        private Optional<Long> worldSeed = Optional.empty();
        private Optional<String> worldName = Optional.empty();
        private Optional<Float> daySpeed = Optional.empty();

        public Builder vSync(boolean vSync){
            this.vSync = Optional.of(vSync);
            return this;
        }

        public Builder debug(boolean debug){
            this.debug = Optional.of(debug);
            return this;
        }

        public Builder fullscreen(boolean fullscreen){
            this.fullscreen = Optional.of(fullscreen);
            return this;
        }

        public Builder width(int width){
            this.width = Optional.of(width);
            return this;
        }

        public Builder height(int height){
            this.height = Optional.of(height);
            return this;
        }

        public Builder renderingDistance(int renderingDistance){
            this.renderingDistance = Optional.of(renderingDistance);
            return this;
        }

        public Builder worldSeed(long worldSeed){
            this.worldSeed = Optional.of(worldSeed);
            return this;
        }

        public Builder worldName(String worldName){
            if (worldName == null || worldName.isEmpty()) {
                throw new IllegalArgumentException();
            }
            this.worldName = Optional.of(worldName);
            return this;
        }

        public Builder daySpeed(float daySpeed) {
            this.daySpeed = Optional.of(daySpeed);
            return this;
        }

        public Settings build() {
            return new Settings(vSync.orElse(true), debug.orElse(false),
                    fullscreen.orElse(true), width.orElse(1920), height.orElse(1080),
                    renderingDistance.orElse(1), worldSeed.orElse(424243563456L),
                    worldName.orElse("world-1"), daySpeed.orElse(10f));
        }
    }
}
