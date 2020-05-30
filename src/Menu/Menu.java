package Menu;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sample.Launcher;
import sample.Pizzeria;
import sample.Settings;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Menu.Menu.ButtonName.*;
import static Menu.Menu.Mode.*;
import static Menu.SoundHandler.Sound.*;
import static sample.Launcher.*;
import static sample.Settings.*;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Menu extends StackPane {
    private static Mode mode = START;
    private static Rectangle bg = new Rectangle(Settings.WIDTH, Settings.HEIGHT);
    private static VBox primaryItems = new VBox();
    private static VBox secondaryItems = new VBox();
    private static Pane floatiesContainer = new Pane();
    private static MenuItem selection;

    private ObservableList<Image> floaties = FXCollections.observableArrayList();
    public static List<MenuItem> buttons = new ArrayList<>();
    private List<MenuItem> activeButtons = new ArrayList<>();
    private List<MenuItem> primaryButtons = new ArrayList<>();
    private List<MenuItem> secondaryButtons = new ArrayList<>();

    public enum Mode {START, PAUSE, RUNNING}

    public enum ButtonName {Start, Quit}

    public Menu() {
        setProperties();
        ButtonBindings.bind();
        getChildren().addAll(bg, floatiesContainer, primaryItems, secondaryItems);
//        pizzeria.addTask("Floaties", 1, () -> goFloaty(makeFloatie(), floatiesContainer), false);
    }

    public void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(Start, Quit));
        primaryItems.getChildren().addAll(primaryButtons);
        setActiveButtons(primaryButtons);
        moveButtons(0, primaryButtons, null);
    }

    private void setProperties() {
        this.setPrefSize(WIDTH, HEIGHT);
        bg.setFill(WHITE);
        primaryItems.setSpacing(10);
        secondaryItems.setSpacing(10);
        primaryItems.setAlignment(Pos.CENTER);
        secondaryItems.setAlignment(Pos.CENTER);
    }

    private void setActiveButtons(List<MenuItem> newButtons) {
        activeButtons.forEach(b -> b.setDefault());
        activeButtons.clear();
        activeButtons.addAll(newButtons);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void extendWith(List<MenuItem> newButtons) {
        secondaryItems.getChildren().clear();
        secondaryButtons.addAll(newButtons);
        secondaryItems.getChildren().addAll(secondaryButtons);
        moveButtons(0, secondaryButtons, () -> setActiveButtons(secondaryButtons));
        moveButtons(-WIDTH, primaryButtons, null);
    }

    private void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons, () -> setActiveButtons(primaryButtons));
        moveButtons(WIDTH, secondaryButtons, () -> {
            secondaryButtons.forEach(b -> b.setDefault());
            secondaryButtons.clear();
        });
    }

    public void switchSelection(int dir) {
        soundHandler.playSound(buttonHover);
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void select() {
        buttons.stream().filter(b -> b == selection).findFirst().get().execute();
        soundHandler.playSound(buttonSelect);
    }

    public void rightArrow() {
        soundHandler.playSound(denied);
    }

    public void leftArrow() {
        if (secondaryButtons.size() > 0) closeExtension();
        else soundHandler.playSound(denied);
    }

    public void toggleMenu() {
        if (mode == START) closeExtension();
        else if (mode == PAUSE) resume();
        else if (mode == RUNNING) pause();
    }
//
//    private void toggleMenu() {
//        if (currentState == menu) {
//            new Thread(pizzeria::startWorking).start();
//        } else if (currentState == program) {
//            pizzeria.close();
//            System.out.println("close");
//        }
//        currentState = !currentState;
//    }

    public void startGame(int level) {
        pizzeria.startWorking();
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(400), menu);
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setOnFinished((event) -> {
            primaryItems.getChildren().clear();
            primaryButtons.clear();
            primaryButtons.addAll(getButtons(Start, Quit));
            primaryItems.getChildren().addAll(primaryButtons);
            closeExtension();
        });
        ft.play();
    }


    private static void pause() {
        mode = PAUSE;
        bg.setOpacity(0.3);
        bg.setFill(BLACK);
        FadeTransition ft = new FadeTransition(new Duration(200), menu);
        ft.setToValue(1);
        ft.play();
        pizzeria.pause();
    }

    public static void resume() {
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(200), menu);
        ft.setToValue(0);
        ft.play();
        pizzeria.resume();
    }

    public void quit() {
        FillTransition fit = new FillTransition(new Duration(1200), bg);
        FadeTransition fat = new FadeTransition(new Duration(1000), floatiesContainer);
        fat.setToValue(0);
        fat.play();
        fit.setToValue(BLACK);
        fit.setInterpolator(Interpolator.EASE_IN);
        fit.play();
        fit.setOnFinished((e) -> System.exit(0));
        moveButtons(WIDTH, activeButtons, null);
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void goFloaty(Pane floatieBox, Pane pane) {
        float safeBuffer = 4 * floatieSize;
        float maxDist = Math.max(WIDTH, HEIGHT) + safeBuffer;
        Random random = new Random();
        int fromX = random.nextInt((int) maxDist);
        int toX = random.nextInt((int) maxDist);
        float fromY = random.nextInt(2) * (maxDist + safeBuffer) - safeBuffer;
        float toY = Math.abs(fromY - maxDist) - safeBuffer;
        double timeMs = random.nextInt(4000) + 5000;
        double rotation = random.nextInt(500) + 360;

        TranslateTransition tr = new TranslateTransition(new Duration(timeMs), floatieBox);
        RotateTransition rt = new RotateTransition(new Duration(timeMs), floatieBox);
        rt.setToAngle(rotation);
        rt.play();

        if (random.nextBoolean()) {
            tr.setFromX(fromX);
            tr.setFromY(fromY);
            tr.setToX(toX);
            tr.setToY(toY);
        } else {
            tr.setFromX(fromY);
            tr.setFromY(fromX);
            tr.setToX(toY);
            tr.setToY(toX);
        }
        pane.getChildren().add(floatieBox);
        tr.setInterpolator(Interpolator.LINEAR);
        tr.setOnFinished((event) -> pane.getChildren().remove(floatieBox));
        tr.play();
    }

    private void moveButtons(int to, List<MenuItem> items, Task task) {
        for (int i = 0; i < items.size(); i++) {
            TranslateTransition tr = new TranslateTransition();
            tr.setInterpolator(Interpolator.EASE_OUT);
            tr.setDuration(new Duration(500));
            tr.setToX(to);
            tr.setNode(items.get(i));
            tr.setDelay(new Duration(100 + i * 100));
            tr.play();
        }
        if (task != null) task.execute();
    }

    public List<MenuItem> getButtons(ButtonName... names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.name));
        return buttonsStream.collect(Collectors.toList());
    }

    private MenuItem getButton(ButtonName name) {
        return buttons.stream().filter(b -> b.name == name).findFirst().get();
    }

    public Mode getMode() {
        return mode;
    }

}

