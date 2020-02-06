package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class GameLauncher extends Application {

    private Stage primaryStage;

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.initView();
        this.primaryStage.showAndWait();
    }

    private void initView() {
        this.primaryStage.setTitle("Cubic World Simulator");
        this.primaryStage.setWidth(1920);
        this.primaryStage.setHeight(1080);
        this.primaryStage.initModality(Modality.WINDOW_MODAL);
        Button launchGameButton = new Button("Launch game");
        launchGameButton.setStyle("-fx-font-size: 15pt;");
        launchGameButton.setOnAction( actionEvent -> {
            this.runGame();
        });
        launchGameButton.setAlignment(Pos.CENTER);
        Scene scene = new Scene(launchGameButton, 200, 100);
        this.primaryStage.setScene(scene);
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
