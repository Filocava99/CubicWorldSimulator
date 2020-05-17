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
}
