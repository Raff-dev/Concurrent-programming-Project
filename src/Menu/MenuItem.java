package Menu;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static Menu.Menu.*;
import static javafx.scene.paint.Color.CADETBLUE;
import static javafx.scene.paint.Color.WHITE;
import static Mechanism.Settings.*;

public class MenuItem extends StackPane {
    private ButtonName buttonName;
    private String name;
    private Task primaryTask;
    private Task secondaryTask;
    private boolean isDual = false;
    private Text text = new Text();
    private Rectangle bg = new Rectangle();

    public MenuItem(ButtonName name, Task task) {
        this.buttonName = name;
        this.primaryTask = task;
        this.secondaryTask = task;
        this.name = String.valueOf(name).replace("_", " ");
        this.text.setText(this.name);
        setDefault();
        setTranslateX(WIDTH);
        getChildren().addAll(bg, text);
    }

    public MenuItem(ButtonName name, long initialValue, Task primaryTask, Task secondaryTask) {
        this.buttonName = name;
        this.primaryTask = primaryTask;
        this.secondaryTask = secondaryTask;
        this.isDual = true;
        this.name = String.valueOf(name).replace("_", " ");
        updateValueText(initialValue);
        setDefault();
        setTranslateX(WIDTH);
        getChildren().addAll(bg, text);
    }

    void setDefault() {
        FadeTransition ft = new FadeTransition(new Duration(50), this);
        ScaleTransition st = new ScaleTransition(new Duration(50), this);
        ft.setToValue(0.8);
        ft.play();
        st.setToX(1);
        st.setToY(1);
        st.play();
        bg.setWidth(WIDTH * 0.7);
        bg.setHeight(50);
        bg.setFill(CADETBLUE);
        text.setFont(Font.font(30));
        text.setFill(WHITE);
    }

    void setHovered() {
        FadeTransition ft = new FadeTransition(new Duration(100), this);
        ScaleTransition st = new ScaleTransition(new Duration(100), this);
        ft.setToValue(1);
        st.setToX(1.2);
        st.setToY(1.2);
        st.play();
        ft.play();
    }

    public ButtonName getButtonName() {
        return buttonName;
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void updateValueText(long value) {
        this.text.setText(this.buttonName + ": " + value);
    }

    boolean isDual() {
        return isDual;
    }

    public void execute(boolean primary) {
        if (primary) primaryTask.execute();
        else secondaryTask.execute();
    }
}
