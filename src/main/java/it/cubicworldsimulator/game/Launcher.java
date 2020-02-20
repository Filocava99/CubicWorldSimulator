package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameEngine;
import lwjgui.LWJGUIApplication;
import lwjgui.geometry.Pos;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryUtil;

import javax.swing.*;
import java.util.Objects;

public class Launcher extends LWJGUIApplication {

    private Button startGame;
    private Button settings;
    private VBox box;
    private GameEngine gameEngine;

    public Launcher(final String[] args, GameEngine gameEngine) {
        this.gameEngine=gameEngine;
        launch(args);
    }

    public Launcher() {}

    @Override
    public void start(String[] args, Window window) {
        // Create background pane
        StackPane pane = new StackPane();

        VBox box = new VBox();
        box.setSpacing(8);
        box.setAlignment(Pos.CENTER);
        pane.getChildren().add(box);

        this.startGame = new Button("Start game");
        startGame.setFontSize(40);
        //TODO PERCHÉ CAZZO GAMEENGINE É NULL
        System.out.println(this.gameEngine);
        startGame.setOnMouseClicked(event -> {
            gameEngine.run();
           //TODO Close launcher window
            //window.closeDisplay();
        });

        this.settings = new Button("Settings");
        settings.setFontSize(40);
        settings.setOnAction(event -> {
            box.getChildren().remove(startGame);
            box.getChildren().remove(settings);
            Label optionsLabel = new Label("Settings");
            optionsLabel.setFontSize(30);
            box.getChildren().add(optionsLabel);
        });

        box.getChildren().add(startGame);
        box.getChildren().add(settings);

        // Set the scene
        window.setScene(new Scene(pane, 500, 500));
        window.show();

    }
}
