package Menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Mechanism.Pizzeria;

import static Mechanism.Settings.*;

public class Launcher extends Application {
    public static final Pizzeria pizzeria = new Pizzeria(seatsSetting);
    public static final Menu menu = new Menu();
    private static final Pane window = new Pane(pizzeria, menu);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);
    public static final InputHandler inputHandler = new InputHandler();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Pizzeria");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        menu.init();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
