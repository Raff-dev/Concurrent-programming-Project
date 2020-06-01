package Mechanism;

import Menu.Assignment;

import java.util.ArrayList;

import static Menu.Menu.Mode.RUNNING;
import static Menu.Launcher.menu;

public class Owner extends Thread {
    /*
    Owner is a program component which is responsible for optimization of table assignment
        owner as a fair man assigns tables in a following manner:
        - customers, who joined queue first, have a higher priority
        - first of all, the owner looks for a best fit
        - if no optimal fit was found, search for any possible assignment at the moment begins
     */
    private Pizzeria pizzeria;
    private final ArrayList<CustomerGroup> awaitingCustomers = new ArrayList<>();
    private volatile int numWaiting = 0;

    private Assignment optimal = (g, t) -> t.isOptimal(g.getNumCustomers());
    private Assignment possible = (g, t) -> t.canHaveASeat(g.getNumCustomers());


    public Owner(Pizzeria pizzeria) {
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        while (true) {
            if (menu.getMode() == RUNNING) checkQueue();
        }
    }

    void checkQueue() {
        ArrayList<Table> tables = pizzeria.getTables();
        ArrayList<CustomerGroup> toRemove = new ArrayList<>();
        boolean updateVisualisation = false;
        try {
            synchronized (awaitingCustomers) {
                for (Assignment assignment : new Assignment[]{optimal, possible}) {
                    if (awaitingCustomers.size() == 0) break;
                    for (CustomerGroup group : awaitingCustomers) {
                        for (Table table : tables) {
                            if (assignment.canAssign(group, table)) {
                                group.joinTable(table);
                                toRemove.add(group);
                                updateVisualisation = true;
                                break;
                            }
                        }
                    }
                    awaitingCustomers.removeAll(toRemove);
                    numWaiting = awaitingCustomers.size();
                }
                if (updateVisualisation) awaitingCustomers.forEach(group ->
                        group.moveInQueue(awaitingCustomers.indexOf(group)));
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println("owner");
        }
    }

    void queueCustomerGroup(CustomerGroup customerGroup) {
        try {
            synchronized (awaitingCustomers) {
                awaitingCustomers.add(customerGroup);
                awaitingCustomers.forEach(group ->
                        group.moveInQueue(awaitingCustomers.indexOf(group)));
//            System.out.println(customerGroup.identify() + " added to queue");
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println("queuing");
        }
    }

    int getNumWaiting() {
        return numWaiting;
    }
}
