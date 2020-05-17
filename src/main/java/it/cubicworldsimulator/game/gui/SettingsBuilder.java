package it.cubicworldsimulator.game.gui;

import java.util.Optional;

/**
 * Build a settings object by using builder design pattern.
 */
public class SettingsBuilder {
    private Optional<Boolean> vSync = Optional.empty();
    private Optional<Boolean> debug = Optional.empty();
    private Optional<Boolean> fullscreen = Optional.empty();
    private Optional<Integer> width = Optional.empty();
    private Optional<Integer> height = Optional.empty();
    private Optional<Integer> renderingDistance = Optional.empty();
    private Optional<Long> worldSeed = Optional.empty();
    private Optional<String> worldName = Optional.empty();

    public SettingsBuilder vSync(boolean vSync){
        this.vSync = Optional.ofNullable(vSync);
        return this;
    }

    public SettingsBuilder debug(boolean debug){
        this.debug = Optional.ofNullable(debug);
        return this;
    }

    public SettingsBuilder fullscreen(boolean fullscreen){
        this.fullscreen = Optional.ofNullable(fullscreen);
        return this;
    }

    public SettingsBuilder width(int width){
        this.width = Optional.ofNullable(width);
        return this;
    }

    public SettingsBuilder height(int height){
        this.height = Optional.ofNullable(height);
        return this;
    }

    public SettingsBuilder renderingDistance(int renderingDistance){
        this.renderingDistance = Optional.ofNullable(renderingDistance);
        return this;
    }

    public SettingsBuilder worldSeed(long worldSeed){
        this.worldSeed = Optional.ofNullable(worldSeed);
        return this;
    }

    public SettingsBuilder worldName(String worldName){
        this.worldName = Optional.ofNullable(worldName);
        return this;
    }

    public Settings build() {
        return new Settings(vSync, debug, fullscreen, width, height,
                renderingDistance, worldSeed, worldName);
    }
}
