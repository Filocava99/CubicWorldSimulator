package it.cubicworldsimulator.game;


import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        boolean vSync = true;
        GameLogic gameLogic = new Game();
        GameEngine gameEng = null;
        try {
            gameEng = new GameEngine("CubicWorldSimulator", 600, 480, vSync, gameLogic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameEng.run();
    }
}
