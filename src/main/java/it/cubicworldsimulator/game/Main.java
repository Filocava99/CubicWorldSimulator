package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean debug = false;
        if(args.length>1 && args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("true")){
            debug = Boolean.parseBoolean(args[0]);
        }
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
