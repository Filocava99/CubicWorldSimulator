package it.cubicworldsimulator.game.gui;

import java.util.Optional;

/**
 * Build a settings object by using builder design pattern.
 */
public class SettingsBuilder {
    Optional<Boolean> vSync = Optional.empty();
    Optional<Boolean> debug = Optional.empty();
    Optional<Boolean> fullscreen = Optional.empty();
    Optional<Integer> width = Optional.empty();
    Optional<Integer> height = Optional.empty();
    Optional<Integer> renderingDistance = Optional.empty();
    Optional<Long> worldSeed = Optional.empty();
    Optional<String> worldName = Optional.empty();

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
