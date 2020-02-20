package it.cubicworldsimulator.game;

import lwjgui.LWJGUIApplication;
import lwjgui.geometry.Pos;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;

public class Launcher extends LWJGUIApplication {

    public Launcher(final String[] args) {
        launch(args);
    }

    public Launcher() {

    }

    @Override
    public void start(String[] args, Window window) {
        // Create background pane
        StackPane pane = new StackPane();

        // Create a horizontal layout
        HBox hbox = new HBox();
        hbox.setSpacing(8);
        pane.getChildren().add(hbox);

            VBox vbox = new VBox();
            vbox.setSpacing(8);
            vbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(vbox);

            Button startGame = new Button("Start game");
            startGame.setOnAction( (event)-> {
                window.close();
            });

            Label optionsLabel = new Label("Settings");
            optionsLabel.setFontSize(30);
            vbox.getChildren().add(startGame);
            vbox.getChildren().add(optionsLabel);
        // Set the scene
        window.setScene(new Scene(pane, 500, 500));
        window.show();
    }
}
