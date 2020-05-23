package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static sample.Settings.*;

public class Main extends Application {

    public static final Pizzeria pizzeria = new Pizzeria(new int[]{2, 1, 3, 2});
    public static final Settings sideBar = new Settings();
    private static final Pane window = new Pane(pizzeria, sideBar);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Pizzeria");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
        pizzeria.startWorking();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
