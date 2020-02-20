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

public class Options extends LWJGUIApplication {
    public Options(final String[] args) {
        launch(args);
    }

    public Options() {}

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

        Label title = new Label("Options");

        vbox.getChildren().add(title);


        // Set the scene
        window.setScene(new Scene(pane, 500, 500));
        window.show();
    }
}
