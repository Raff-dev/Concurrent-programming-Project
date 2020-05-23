package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static sample.Settings.*;


public class Table extends StackPane {
    private int seatCount;
    private int occupiedCount = 0;
    private int groupSize = 0;
    private int id;

    private Text text = new Text();

    Table(int seatCount, int id) {
        this.seatCount = seatCount;
        this.id = id;
        System.out.println(seatCount + ":" + id);
        createTable();
    }

    private void createTable() {
        setTranslateX(getX());
        setTranslateY(getY());

        System.out.println("REEEEEEEEEEEEEEEEEEEEEEEE");

        text.setText(occupiedCount + "/" + seatCount + "|" + groupSize);
        text.setFont(new Font(20 * getScale()));
        text.setFill(Color.WHITE);

        Rectangle rect = new Rectangle(getTableSize(), getTableSize());
        rect.setFill(Color.BLACK);

        getChildren().add(rect);
        getChildren().add(text);
        Main.pizzeria.getChildren().add(this);
    }

    void joinTable(int customersCount) {
        synchronized (this) {
            if (groupSize == 0) groupSize = customersCount;
            if (groupSize != customersCount)
                throw new AssertionError("those customers shouldnt be seated here");
            if (seatCount - occupiedCount < customersCount)
                throw new AssertionError("too many joined the table");
            occupiedCount += customersCount;
            text.setText(occupiedCount + "/" + seatCount + "|" + groupSize);
        }
    }

    void leaveTable(int customersCount) {
        synchronized (this) {
            occupiedCount -= customersCount;
            if (occupiedCount < 0)
                throw new AssertionError("too many left the table");
            if (occupiedCount == 0) groupSize = 0;
            text.setText(occupiedCount + "/" + seatCount + "|" + groupSize);
        }
    }

    int getFreeSeatsCount() {
        if (seatCount < occupiedCount)
            throw new AssertionError("too many customers at the table" + occupiedCount + "/" + seatCount);
        return seatCount - occupiedCount;
    }

    public boolean canHaveASeat(int customersCount) {
        boolean isFree = occupiedCount == 0;
        boolean hasEnoughSeats = getFreeSeatsCount() >= customersCount;
        boolean matchingGroupSize = customersCount == groupSize;
        return (isFree && hasEnoughSeats) || (matchingGroupSize && hasEnoughSeats);
    }

    public boolean isOptimal(int customersCount) {
        return canHaveASeat(customersCount) && seatCount - occupiedCount == customersCount;
    }

    String identify() {
        return "ID (" + seatCount + "," + id + ") C: " + occupiedCount + " S:" + groupSize + " | ";
    }

    float getX() {
        return tableSpacing * (1 + id) * scale;
    }

    float getY() {
        return tableSpacing * (1 + seatCount) * scale;
    }

    void onScaleChange() {

    }
}
