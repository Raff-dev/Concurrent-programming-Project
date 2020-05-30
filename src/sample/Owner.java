package sample;

import Menu.Menu;

import java.util.ArrayList;

import static Menu.Menu.Mode.RUNNING;
import static sample.Launcher.menu;

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
            else if (awaitingCustomers.size()>0) {
                awaitingCustomers.forEach(Thread::interrupt);
                awaitingCustomers.clear();
                numWaiting = 0;
            }
            awaitingCustomers.forEach(customerGroup -> System.out.println("chuj"));
        }
    }

    void checkQueue() {
        ArrayList<Table> tables = pizzeria.getTables();
        ArrayList<CustomerGroup> toRemove = new ArrayList<>();
        boolean updateVisualisation = false;

        synchronized (awaitingCustomers) {
//            int numThreads = java.lang.Thread.activeCount();
//            if (numThreads != lastNumThreads){
//                lastNumThreads = numThreads;
////                System.out.println("Active threads count: " + numThreads);
//            }
//            if (awaitingCustomers.size() > 0)
//                System.out.println(awaitingCustomers.size());
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
    }

    void queueCustomerGroup(CustomerGroup customerGroup) {
        synchronized (awaitingCustomers) {
            awaitingCustomers.add(customerGroup);
            awaitingCustomers.forEach(group ->
                    group.moveInQueue(awaitingCustomers.indexOf(group)));
//            System.out.println(customerGroup.identify() + " added to queue");

        }
    }

    int getNumWaiting() {
        return numWaiting;
    }
}
