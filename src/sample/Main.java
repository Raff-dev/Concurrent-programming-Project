package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

import static sample.Settings.*;

public class Main extends Application {

    public static final Pizzeria pizzeria = new Pizzeria(new int[]{1, 1, 1, 1});
    public static final Settings sideBar = new Settings();
    private static final Pane window = new Pane(pizzeria, sideBar);
    public static final Scene scene = new Scene(window, WIDTH, HEIGHT);

    @Override
    public void start(Stage stage) {
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1,2,3));
        final ArrayList<Integer> arr2;
        arr2= new ArrayList<>(arr);
        arr.add(4);
        System.out.println(arr2.toString());

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
