package Menu;

import Mechanism.Settings;

import static Menu.Menu.*;
import static Menu.Menu.ButtonName.*;
import static Menu.Launcher.menu;

public class BindingsHandler {
    public static void bind() {
        buttons.add(new MenuItem(Resume, () -> menu.resume()));
        buttons.add(new MenuItem(Start, () -> menu.startWorking()));
        buttons.add(new MenuItem(Tables, () -> menu.extendWith(
                menu.getButtons(One_Seat, Two_Seat, Three_Seat, Four_Seat))));
        buttons.add(new MenuItem(Customers, () -> menu.extendWith(
                menu.getButtons(Spawn_Time, Spawn_Time_Variation, Eating_Time, Max_Queue_Size))));
        buttons.add(new MenuItem(Quit, () -> menu.quit()));

        buttons.add(new MenuItem(One_Seat, Settings.getSeatsSetting()[0],
                () -> Settings.setSeatsSetting(0, -1),
                () -> Settings.setSeatsSetting(0, 1)));
        buttons.add(new MenuItem(Two_Seat, Settings.getSeatsSetting()[1],
                () -> Settings.setSeatsSetting(1, -1),
                () -> Settings.setSeatsSetting(1, 1)));
        buttons.add(new MenuItem(Three_Seat, Settings.getSeatsSetting()[2],
                () -> Settings.setSeatsSetting(2, -1),
                () -> Settings.setSeatsSetting(2, 1)));
        buttons.add(new MenuItem(Four_Seat, Settings.getSeatsSetting()[3],
                () -> Settings.setSeatsSetting(3, -1),
                () -> Settings.setSeatsSetting(3, 1)));

        buttons.add(new MenuItem(Spawn_Time, Settings.getCustomerSpawnTime(),
                () -> Settings.setCustomerSpawnTime(-20),
                () -> Settings.setCustomerSpawnTime(20)));
        buttons.add(new MenuItem(Spawn_Time_Variation, Settings.getCustomerSpawnTimeVariation(),
                () -> Settings.setCustomerSpawnTimeVariation(-20),
                () -> Settings.setCustomerSpawnTimeVariation(20)));
        buttons.add(new MenuItem(Eating_Time, Settings.getEatingTime(),
                () -> Settings.setEatingTime(-50),
                () -> Settings.setEatingTime(50)));
        buttons.add(new MenuItem(Max_Queue_Size, Settings.getMaxQueueSize(),
                () -> Settings.setMaxNumWaiting(-1),
                () -> Settings.setMaxNumWaiting(1)));
    }
}
