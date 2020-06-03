package Mechanism;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

import static Menu.Menu.Mode.*;
import static Menu.Launcher.menu;
import static Mechanism.Settings.*;

public class Pizzeria extends Pane implements Runnable {

    private Owner owner;
    private final ArrayList<Table> tables = new ArrayList<>();
    private int[] tablesSeatCounts;
    private int differentTablesCount = 4;
    private int customerGroupCounter = 0;

    public Pizzeria(int[] tablesSeatCounts) {
        assert tablesSeatCounts.length == differentTablesCount;
        this.tablesSeatCounts = tablesSeatCounts;
    }

    @Override
    public void run() {
        generateTables();
        owner = new Owner(this);
        owner.start();
        while (true) if (menu.getMode() == RUNNING) {
            generateCustomer();
            try {
                Thread.sleep(getRandom().nextInt(customerSpawnTimeVariation) + customerSpawnTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateTables() {
        for (int seatCount = 1; seatCount <= differentTablesCount; seatCount++)
            for (int tableIndex = 0; tableIndex < tablesSeatCounts[seatCount - 1]; tableIndex++)
                tables.add(new Table(seatCount, tableIndex));
    }

    private void generateCustomer() {
            int numWaiting = owner.getNumWaiting();
            if (numWaiting < Settings.maxNumWaiting) {
                customerGroupCounter++;
                int customersCount = getRandom().nextInt(3) + 1;
                CustomerGroup customerGroup = new CustomerGroup(this, owner, customersCount, customerGroupCounter);
                customerGroup.start();
            }
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
}
