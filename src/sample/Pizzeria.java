package sample;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

import static sample.Settings.*;

public class Pizzeria extends Pane {

    private final Owner owner = new Owner(this);
    private final ArrayList<Table> tables = new ArrayList<>();
    private int[] tablesSeatCounts;
    private boolean isRunning = true;
    private int differentTablesCount = 4;
    private int customerGroupCounter = 0;

    Pizzeria(int[] tablesSeatCounts) {
        assert tablesSeatCounts.length == differentTablesCount;
        this.tablesSeatCounts = tablesSeatCounts;
    }

    void startWorking() {
        generateTables();
        owner.start();
        new Thread(this::generateCustomers).start();
    }

    private void generateTables() {
        for (int seatCount = 1; seatCount <= differentTablesCount; seatCount++)
            for (int tableIndex = 0; tableIndex < tablesSeatCounts[seatCount - 1]; tableIndex++)
                tables.add(new Table(seatCount, tableIndex));
    }

    private void generateCustomers() {
        while (isRunning) {
            int numWaiting = owner.getNumWaiting();
            if (numWaiting < Settings.maxNumWaiting) {
                customerGroupCounter++;
                int customersCount = getRandom().nextInt(3) + 1;
                CustomerGroup customerGroup = new CustomerGroup(this, owner, customersCount, customerGroupCounter);
                customerGroup.start();
            } else System.out.println("Max number of waiting customers reached: " + numWaiting);
            try {
                Thread.sleep(getRandom().nextInt(newCustomersTimeVariation) + newCustomersTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}
