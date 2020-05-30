package sample;

import Menu.Menu;
import Menu.InputHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import static sample.Settings.*;
import Menu.SoundHandler;

public class Launcher extends Application {
    public static final Pizzeria pizzeria = new Pizzeria(seatsSetting);
    public static final Menu menu = new Menu();
    private static final Pane window = new Pane(pizzeria, menu);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);
    public static final InputHandler inputHandler = new InputHandler();
    public static final SoundHandler soundHandler = new SoundHandler();


    @Override
    public void start(Stage stage) {
        stage.setTitle("Pizzeria");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        menu.init();
        new Thread(pizzeria).start();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
