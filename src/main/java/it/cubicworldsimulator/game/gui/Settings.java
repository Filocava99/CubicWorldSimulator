package it.cubicworldsimulator.game.gui;

import java.util.Optional;

public class Settings {
    private Optional<Boolean> vSync = Optional.empty();
    private Optional<Boolean> debug = Optional.empty();
    private Optional<Boolean> fullscreen = Optional.empty();
    private Optional<Integer> width = Optional.empty();
    private Optional<Integer> height = Optional.empty();
    private Optional<Integer> renderingDistance = Optional.empty();
    private Optional<Long> worldSeed = Optional.empty();
    private Optional<String> worldName = Optional.empty();

    public Settings(Optional<Boolean> vSync, Optional<Boolean> debug, Optional<Boolean> fullscreen, Optional<Integer> width, Optional<Integer> height, Optional<Integer> renderingDistance, Optional<Long> worldSeed, Optional<String> worldName) {
        this.vSync = vSync;
        this.debug = debug;
        this.fullscreen = fullscreen;
        this.width = width;
        this.height = height;
        this.renderingDistance = renderingDistance;
        this.worldSeed = worldSeed;
        this.worldName = worldName;
    }

    public Optional<Boolean> getvSync() {
        return vSync;
    }

    public Optional<Boolean> getDebug() {
        return debug;
    }

    public Optional<Boolean> getFullscreen() {
        return fullscreen;
    }

    public Optional<Integer> getWidth() {
        return width;
    }

    public Optional<Integer> getHeight() {
        return height;
    }

    public Optional<Integer> getRenderingDistance() {
        return renderingDistance;
    }

    public Optional<Long> getWorldSeed() {
        return worldSeed;
    }

    public Optional<String> getWorldName() {
        return worldName;
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
            this.debug = Optional.ofNullable(debug);
            return this;
        }

        public Builder fullscreen(boolean fullscreen){
            this.fullscreen = Optional.ofNullable(fullscreen);
            return this;
        }

        public Builder width(int width){
            this.width = Optional.ofNullable(width);
            return this;
        }

        public Builder height(int height){
            this.height = Optional.ofNullable(height);
            return this;
        }

        public Builder renderingDistance(int renderingDistance){
            this.renderingDistance = Optional.ofNullable(renderingDistance);
            return this;
        }

        public Builder worldSeed(long worldSeed){
            this.worldSeed = Optional.ofNullable(worldSeed);
            return this;
        }

        public Builder worldName(String worldName){
            this.worldName = Optional.ofNullable(worldName);
            return this;
        }

        public Settings build() {
            return new Settings(vSync, debug, fullscreen, width, height,
                    renderingDistance, worldSeed, worldName);
        }
    }
}
