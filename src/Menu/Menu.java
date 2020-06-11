package Menu;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import Mechanism.Settings;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Menu.Menu.ButtonName.*;
import static Menu.Menu.Mode.*;
import static Menu.Launcher.*;
import static Mechanism.Settings.*;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Menu extends StackPane {
    private static volatile Mode mode = START;
    private static Rectangle bg = new Rectangle(Settings.WIDTH, Settings.HEIGHT);
    private static VBox primaryItems = new VBox();
    private static VBox secondaryItems = new VBox();
    private static Pane floatiesContainer = new Pane();
    private static MenuItem selection;

    public static List<MenuItem> buttons = new ArrayList<>();
    private List<MenuItem> activeButtons = new ArrayList<>();
    private List<MenuItem> primaryButtons = new ArrayList<>();
    private List<MenuItem> secondaryButtons = new ArrayList<>();

    private ArrayList<ButtonName> startButtons
            = new ArrayList<>(Arrays.asList(Start, Tables, Customers, Quit));
    private ArrayList<ButtonName> pauseButtons
            = new ArrayList<>(Arrays.asList(Resume, Customers, Quit));

    public enum Mode {START, PAUSE, RUNNING}

    public enum ButtonName {
        Start, Resume, Quit, Tables, Customers,
        One_Seat, Two_Seats, Three_Seats, Four_Seats,
        Spawn_Time, Spawn_Time_Variation, Eating_Time, Max_Queue_Size
    }

    public Menu() {
        setProperties();
        BindingsHandler.bind();
        getChildren().addAll(bg, floatiesContainer, primaryItems, secondaryItems);
    }

    public void init() {
        selection = buttons.get(0);
        selection.setHovered();
        primaryButtons.addAll(getButtons(startButtons));
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

    void closeExtension() {
        if (secondaryButtons.size() == 0) return;
        moveButtons(0, primaryButtons, () -> setActiveButtons(primaryButtons));
        moveButtons(WIDTH, secondaryButtons, () -> {
            secondaryButtons.forEach(b -> b.setDefault());
            secondaryButtons.clear();
        });
    }

    public void switchSelection(int dir) {
        selection.setDefault();
        Collections.rotate(activeButtons, -dir);
        selection = activeButtons.get(0);
        selection.setHovered();
    }

    public void select() {
        buttons.stream().filter(b -> b == selection).findFirst().get().execute(true);
    }

    public void leftArrow() {
        if (selection.isDual()) selection.execute(true);
    }

    public void rightArrow() {
        if (selection.isDual()) selection.execute(false);
    }

    public void toggleMenu() {
        if (mode == START) closeExtension();
        else if (mode == PAUSE) resume();
        else if (mode == RUNNING) pause();
    }

    public void startWorking() {
        mode = RUNNING;
        FadeTransition ft = new FadeTransition(new Duration(400), menu);
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setOnFinished((event) -> {
            primaryItems.getChildren().clear();
            primaryButtons.clear();
            primaryButtons.addAll(getButtons(pauseButtons));
            primaryButtons.forEach(b -> {
                b.setTranslateX(0);
            });
            primaryItems.getChildren().addAll(primaryButtons);
        });
        ft.play();
        new Thread(pizzeria).start();
    }

    private void pause() {
        mode = PAUSE;
        bg.setOpacity(0.3);
        bg.setFill(BLACK);
        FadeTransition ft = new FadeTransition(new Duration(200), menu);
        ft.setToValue(1);
        ft.play();
    }

    public void resume() {
        mode = RUNNING;
        closeExtension();
        FadeTransition ft = new FadeTransition(new Duration(200), menu);
        ft.setToValue(0);
        ft.play();
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

    private void moveButtons(int to, List<MenuItem> items, Task task) {
        for (int i = 0; i < items.size(); i++) {
            TranslateTransition tr = new TranslateTransition();
            tr.setInterpolator(Interpolator.EASE_OUT);
            tr.setDuration(new Duration(500));
            tr.setToX(to);
            tr.setNode(items.get(i));
            tr.setDelay(new Duration(100 + i * 100));
            tr.play();
            if (task != null && i == items.size() - 1) tr.setOnFinished((e) -> task.execute());
        }
    }

    public List<MenuItem> getButtons(ButtonName... names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> Arrays.asList(names).contains(b.getButtonName()));
        return buttonsStream.collect(Collectors.toList());
    }

    public List<MenuItem> getButtons(ArrayList<ButtonName> names) {
        Stream<MenuItem> buttonsStream = buttons.stream().filter(
                b -> names.contains(b.getButtonName()));
        return buttonsStream.collect(Collectors.toList());
    }

    public static MenuItem getButton(ButtonName name) {
        return buttons.stream().filter(b -> b.getButtonName() == name).findFirst().get();
    }

    public Mode getMode() {
        return mode;
    }
}

