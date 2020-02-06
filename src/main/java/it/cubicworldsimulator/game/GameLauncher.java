package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameLauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cubic World Simulator");
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        primaryStage.show();
        stage.showAndWait();
    }

    private void runGame() {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new Game();
            GameEngine gameEng = new GameEngine("CubicWorldSimulator", 600, 480, vSync, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
