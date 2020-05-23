package sample;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static sample.Settings.*;

public class CustomerGroup extends Thread {
    private Pizzeria pizzeria;
    private final Owner owner;
    private int numCustomers;
    private volatile Table table = null;
    private int id;
    //    private ArrayList<Customer> customers = new ArrayList<>();
    private StackPane visualisation = new StackPane();

    CustomerGroup(Pizzeria pizzeria, Owner owner, int numCustomers, int customerGroupCounter) {
        this.pizzeria = pizzeria;
        this.owner = owner;
        this.numCustomers = numCustomers;
        this.id = customerGroupCounter;
//        IntStream.range(0, customersCount).forEach(i -> customers.add(new Customer()));
        visualise();
    }

    private void visualise() {
        Rectangle rectangle = new Rectangle(20, 20);
        rectangle.setFill(Color.RED);

        Text text = new Text(id + "|" + numCustomers);
        text.setFont(Font.font(10));

        visualisation.getChildren().add(rectangle);
        visualisation.getChildren().add(text);
        Platform.runLater(() -> pizzeria.getChildren().add(visualisation));
    }

    @Override
    public void run() {
        getInQueue();
//        orderPizza();
        eatPizza();
        leaveTable();
    }

    private void getInQueue() {
        owner.queueCustomerGroup(this);
        while (table == null) {

//            System.out.println("Customer Group: " + id + " size: " + numCustomers + " looking for table");
        }
    }

    void joinTable(Table table) {
        table.joinTable(numCustomers);
        Platform.runLater(() -> visualisation.relocate(table.getX()+Settings.getTableSize()+20,table.getY()));
        this.table = table;
    }

    private void eatPizza() {
        System.out.println("Customer Group: " + id + " size: " + numCustomers + " found " + table.identify());
        try {
            sleep(eatingTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leaveTable() {
        Platform.runLater(() -> pizzeria.getChildren().remove(visualisation));
        table.leaveTable(numCustomers);
//        System.out.println("Customer Group: " + customerGroupCounter +" size: "+customersCount+ " left " + table.identify());
    }

    public void moveInQueue(float offset) {
        Platform.runLater(() -> visualisation.setTranslateX((1 + offset) * getQueueSpacing()));
    }

    public int getNumCustomers() {
        return numCustomers;
    }


    void onScaleChange() {

    }

    public String identify() {
        return "C-" + id + "|" + numCustomers;
    }

    public Table getTable() {
        return table;
    }

    public void setTable() {
        this.table = table;
    }
}
