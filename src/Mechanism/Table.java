package Mechanism;

import Menu.Launcher;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static Mechanism.Settings.*;

public class Table extends StackPane {
    private int seatCount;
    private volatile int occupiedCount = 0;
    private volatile int groupSize = 0;
    private int id;
    private int[] seats = new int[]{0, 0, 0, 0};

    private Text text = new Text();

    Table(int seatCount, int id) {
        this.seatCount = seatCount;
        this.id = id;
        visualise();
    }

    private void visualise() {
        setTranslateX(getX());
        setTranslateY(getY());

        text.setText(occupiedCount + "/" + seatCount + "|" + groupSize);
        text.setFont(new Font(20 * getScale()));
        text.setFill(Color.WHITE);

        Rectangle rect = new Rectangle(getTableSize(), getTableSize());
        rect.setFill(Color.BLACK);

        Platform.runLater(() -> {
            getChildren().add(rect);
            getChildren().add(text);
            Launcher.pizzeria.getChildren().add(this);
        });
    }

    int[] joinTable(int customersCount) {
        synchronized (this) {
            if (groupSize == 0) groupSize = customersCount;
            if (groupSize != customersCount)
                throw new AssertionError("those customers shouldnt be seated here");
            if (seatCount - occupiedCount < customersCount)
                throw new AssertionError("too many joined the table");
            occupiedCount += customersCount;
            ///////////////////////
            Platform.runLater(() -> text.setText(occupiedCount + "/" + seatCount + "|" + groupSize));
            int[] emptySeats = new int[]{0, 0, 0, 0};
            for (int index = 0; index < seats.length; index++) {
                if (customersCount == 0) break;
                if (seats[index] == 0) {
                    seats[index] = 1;
                    emptySeats[index] = 1;
                    customersCount--;
                }
            }
            return emptySeats;
        }
    }

    void leaveTable(int customersCount, int[] occupiedSeats) {
        synchronized (this) {
            occupiedCount -= customersCount;
            if (occupiedCount < 0)
                throw new AssertionError("too many left the table");
            if (occupiedCount == 0) groupSize = 0;
            Platform.runLater(() -> text.setText(occupiedCount + "/" + seatCount + "|" + groupSize));
            for (int index = 0; index < seats.length; index++) {
                if (occupiedSeats[index] == 1) seats[index] = 0;
            }
        }
    }

    public boolean canHaveASeat(int customersCount) {
        synchronized (this) {
            boolean isFree = occupiedCount == 0;
            boolean hasEnoughSeats = getFreeSeatsCount() >= customersCount;
            boolean matchingGroupSize = customersCount == groupSize;
            return (isFree && hasEnoughSeats) || (matchingGroupSize && hasEnoughSeats);
        }
    }

    public boolean isOptimal(int customersCount) {
        synchronized (this) {
            return canHaveASeat(customersCount) && seatCount - occupiedCount == customersCount;
        }
    }

    String identify() {
        return "ID (" + seatCount + "," + id + ") C: " + occupiedCount + " S:" + groupSize + " | ";
    }

    int getFreeSeatsCount() {
        if (seatCount < occupiedCount)
            throw new AssertionError("too many customers at the table" + occupiedCount + "/" + seatCount);
        return seatCount - occupiedCount;
    }

    float getX() {
        return tableSpacing * (1 + id) * scale;
    }

    float getY() {
        return tableSpacing * (1 + seatCount) * scale;
    }

    float getSeatX(int index) {
        float offset = 0;
        if (index == 0) offset = tableSize / 2 - customerSize / 2;
        else if (index == 1) offset = tableSize;
        else if (index == 2) offset = tableSize / 2 - customerSize / 2;
        else if (index == 3) offset = -customerSize;
        return offset;
    }

    float getSeatY(int index) {
        float offset = 0;
        if (index == 0) offset = -customerSize;
        else if (index == 1) offset = tableSize / 2 - customerSize / 2;
        else if (index == 2) offset = tableSize;
        else if (index == 3) offset = tableSize / 2 - customerSize / 2;
        return offset;
    }
}
