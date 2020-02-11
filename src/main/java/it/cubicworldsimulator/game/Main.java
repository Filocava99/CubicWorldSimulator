package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            GameLogic gameLogic = new Game();
            GameEngine gameEngine = new GameEngine("CubicWorldSimulator", 600, 480,
                    true, gameLogic);
                    gameEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
