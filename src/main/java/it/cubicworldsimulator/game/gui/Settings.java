package it.cubicworldsimulator.game.gui;

import java.util.Optional;

/**
 * @author Lorenzo Balzani
 */
public class Settings {
    private final Optional<Boolean> vSync;
    private final Optional<Boolean> debug;
    private final Optional<Boolean> fullscreen;
    private final Optional<Integer> width;
    private final Optional<Integer> height;
    private final Optional<Integer> renderingDistance;
    private final Optional<Long> worldSeed;
    private final Optional<String> worldName;

    public Settings(final Optional<Boolean> vSync, final Optional<Boolean> debug, final Optional<Boolean> fullscreen,
                    final Optional<Integer> width, final Optional<Integer> height, final Optional<Integer> renderingDistance,
                    final Optional<Long> worldSeed, final Optional<String> worldName) {
        this.vSync = vSync;
        this.debug = debug;
        this.fullscreen = fullscreen;
        this.width = width;
        this.height = height;
        this.renderingDistance = renderingDistance;
        this.worldSeed = worldSeed;
        this.worldName = worldName;
    }

    public boolean getvSync() {
        return vSync.orElse(true);
    }

    public boolean getDebug() {
        return debug.orElse(false);
    }

    public boolean getFullscreen() {
        return fullscreen.orElse(true);
    }

    public int getWidth() {
        return width.orElse(1920);
    }

    public int getHeight() {
        return height.orElse(1080);
    }

    public int getRenderingDistance() {
        return renderingDistance.orElse(1);
    }

    public long getWorldSeed() {
        return worldSeed.orElse(424243563456L);
    }

    public String getWorldName() {
        return worldName.orElse("world-1");
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

        public Builder vSync(boolean vSync){
            this.vSync = Optional.ofNullable(vSync);
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

        public Settings build() {
            return new Settings(vSync, debug, fullscreen, width, height,
                    renderingDistance, worldSeed, worldName);
        }
    }
}
