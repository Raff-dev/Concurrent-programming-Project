package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.ListIterator;

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

    public Owner(Pizzeria pizzeria) {
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        while (pizzeria.isRunning()) checkQueue();
    }

    void checkQueue() {
        ArrayList<Table> tables = pizzeria.getTables();
        ArrayList<CustomerGroup> toRemove = new ArrayList<>();
        Assignment optimal = (g, t) -> t.isOptimal(g.getNumCustomers());
        Assignment possible = (g, t) -> t.canHaveASeat(g.getNumCustomers());
        boolean updateVisualisation = false;

        synchronized (awaitingCustomers) {
            if (awaitingCustomers.size() > 0)
                System.out.println(awaitingCustomers.size());
            for (Assignment assignment : new Assignment[]{optimal, possible}) {
                if (awaitingCustomers.size() == 0) break;
                for (CustomerGroup group : awaitingCustomers) {
                    for (Table table : tables) {
                        if (assignment.canAssign(group, table)) {
                            group.joinTable(table);
                            toRemove.add(group);
                            updateVisualisation = true;
                        }
                    }
                }
                awaitingCustomers.removeAll(toRemove);
            }
            if (updateVisualisation) awaitingCustomers.forEach(group ->
                    group.moveInQueue(awaitingCustomers.indexOf(group)));
        }
    }

    void queueCustomerGroup(CustomerGroup customerGroup) {
        synchronized (awaitingCustomers) {
            awaitingCustomers.add(customerGroup);
            System.out.println(customerGroup.identify() + " added to queue");
        }
    }
}
