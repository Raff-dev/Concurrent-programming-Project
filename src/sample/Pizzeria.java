package sample;

import Menu.Menu;
import Menu.Menu.Mode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

import static Menu.Menu.Mode.*;
import static sample.Launcher.menu;
import static sample.Settings.*;

public class Pizzeria extends Pane implements Runnable {

    private Owner owner;
    private final ArrayList<Table> tables = new ArrayList<>();
    private int[] tablesSeatCounts;
    private int differentTablesCount = 4;
    private int customerGroupCounter = 0;

    Pizzeria(int[] tablesSeatCounts) {
        assert tablesSeatCounts.length == differentTablesCount;
        this.tablesSeatCounts = tablesSeatCounts;
    }


    @Override
    public void run() {

    }

    public void resume() {
    }

    public void pause() {
    }

    public void init() {
        generateTables();
        owner = new Owner(this);
        owner.start();
    }


    public void startWorking() {
        if (owner == null) init();
        generateCustomers();
    }

    private void generateTables() {
        for (int seatCount = 1; seatCount <= differentTablesCount; seatCount++)
            for (int tableIndex = 0; tableIndex < tablesSeatCounts[seatCount - 1]; tableIndex++)
                tables.add(new Table(seatCount, tableIndex));
    }

    private void generateCustomers() {
        while (true) {
            Mode mode = menu.getMode();
            if (mode == START || mode == PAUSE) return;
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

    public ArrayList<Table> getTables() {
        return tables;
    }
}
