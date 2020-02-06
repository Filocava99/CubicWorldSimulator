package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import it.cubicworldsimulator.engine.GameLogic;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;


public class GameLauncher extends Application {

    private Stage stage;

    @Override
    public void start(final Stage primaryStage) {
        this.stage = new Stage();
        this.initView();
        this.stage.showAndWait();
    }

    private void initView() {
        this.stage.setTitle("Cubic World Simulator");
        this.stage.setWidth(1920);
        this.stage.setHeight(1080);
        this.stage.initModality(Modality.WINDOW_MODAL);
        Button launchGameButton = new Button("Launch game");
        Button settingsButton = new Button("Settings");
        this.setProprietyButton(launchGameButton);
        this.setProprietyButton(settingsButton);
        launchGameButton.setOnAction( actionEvent -> {
            this.runGame();
        });
        settingsButton.setOnAction( actionEvent -> {
            this.showSettingsWindow();
        });
        TilePane tilePane = new TilePane();
        tilePane.setAlignment(Pos.CENTER);
        tilePane.getChildren().add(launchGameButton);
        tilePane.getChildren().add(settingsButton);
        Scene scene = new Scene(tilePane, 200, 100);
        this.stage.setScene(scene);
    }

    private void setProprietyButton(Button button) {
        button.setStyle("-fx-font-size: 20pt;");
        button.setMaxSize(200, 100);
        button.setAlignment(Pos.CENTER);
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

    //TODO Settings window
    private void showSettingsWindow() {

    }
}
