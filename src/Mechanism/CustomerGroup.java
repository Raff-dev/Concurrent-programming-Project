package Mechanism;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static Mechanism.Settings.*;

public class CustomerGroup extends Thread {
    private Pizzeria pizzeria;
    private final Owner owner;
    private int numCustomers;
    private Table table = null;
    private int id;

    private StackPane visualisation = new StackPane();
    private ArrayList<Rectangle> customers = new ArrayList<>();
    private Rectangle group;
    private int[] occupiedSeats;
    private Color color;

    CustomerGroup(Pizzeria pizzeria, Owner owner, int numCustomers, int customerGroupCounter) {
        if (numCustomers <= 0) throw new AssertionError("invalid number of customers");
        this.pizzeria = pizzeria;
        this.owner = owner;
        this.numCustomers = numCustomers;
        this.id = customerGroupCounter;
        visualise();
    }

    private void visualise() {
        color = getRandomColor();
        group = new Rectangle(customerSize * numCustomers, customerSize);
        group.setFill(color);

        IntStream.range(0, numCustomers).forEach(i -> {
            Rectangle r = new Rectangle(getCustomerSize(), getCustomerSize());
            r.setFill(color);
            customers.add(r);
        });

        Text text = new Text(id + "|" + numCustomers);
        text.setFont(Font.font(10));

        Platform.runLater(() -> {
            visualisation.setTranslateY(HEIGHT - 50);
            visualisation.getChildren().add(group);
                visualisation.getChildren().add(text);
        });
    }

    @Override
    public void run() {
        getInQueue();
        eatPizza();
        leaveTable();
    }

    private void getInQueue() {
        owner.queueCustomerGroup(this);
        Platform.runLater(() -> pizzeria.getChildren().add(visualisation));

        while (table == null) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void joinTable(Table table) {
        Platform.runLater(() -> {
            visualisation.getChildren().remove(group);
            visualisation.setTranslateX(table.getX());
            visualisation.setTranslateY(table.getY());
        });
        this.occupiedSeats = table.joinTable(numCustomers);
        int counter = 0;
        for (int index = 0; index < occupiedSeats.length; index++) {
            if (occupiedSeats[index] == 1) {
                Rectangle customer = customers.get(counter);
                int finalIndex = index;
                Platform.runLater(() -> {
                    customer.setTranslateX(table.getSeatX(finalIndex));
                    customer.setTranslateY(table.getSeatY(finalIndex));
                    visualisation.getChildren().add(customer);
                });
                counter++;
            }
        }
        this.table = table;
    }

    private void eatPizza() {
//        System.out.println("Customer Group: " + id + " size: " + numCustomers + " found " + table.identify());
        try {
            sleep(eatingTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void leaveTable() {
        if (table == null) throw new AssertionError("table null while leving");
//        System.out.println("Customer Group: " + customerGroupCounter +" size: "+customersCount+ " left " + table.identify());
        table.leaveTable(numCustomers, occupiedSeats);
        table = null;
        Platform.runLater(() -> pizzeria.getChildren().remove(visualisation));
    }

    public void moveInQueue(int offset) {
        if (table != null) throw new AssertionError("Customers moving while already out of queue");
        Platform.runLater(() -> visualisation.setTranslateX((1 + offset) * getQueueSpacing()));
    }

    public int getNumCustomers() {
        return numCustomers;
    }
}
