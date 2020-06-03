package Mechanism;

import Menu.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static Menu.Menu.*;
import static Menu.Menu.getButton;

public class Settings extends Pane {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static float scale = 1;
    public static float timeScale = 1;

    public static int[] seatsSetting = new int[]{3, 3, 3, 3};
    public static float customerSize = 20;
    public static float tableSize = 50;
    public static float tableSpacing = 100;
    public static int queueSpacing = 100;

    public static int customerSpawnTimeVariation = (int) (200 * timeScale);
    public static long customerSpawnTime = (long) (300 * timeScale);
    public static long eatingTime = (long) (1000 * timeScale);
    public static int maxNumWaiting = 5;

    private static final Random random = new Random();

    public static void setScale(float scale) {
        Settings.scale = scale;
    }

    public static void setTimeScale(float timeScale) {
        Settings.timeScale = timeScale;
    }

    public static float getScale() {
        return scale;
    }

    public static float getTableSize() {
        return tableSize * scale;
    }

    public static float getQueueSpacing() {
        return queueSpacing * scale;
    }

    public static int getCustomerSpawnTimeVariation() {
        return customerSpawnTimeVariation;
    }

    public static long getCustomerSpawnTime() {
        return customerSpawnTime;
    }

    public static int[] getSeatsSetting() {
        return seatsSetting;
    }

    public static void setSeatsSetting(int index, int change) {
        assert index <= Settings.seatsSetting.length;
        if (Settings.seatsSetting[index] + change >= 0) {
            Settings.seatsSetting[index] += change;
            MenuItem menuItem;
            if (index == 0) menuItem = getButton(ButtonName.One_Seat);
            else if (index == 1) menuItem = getButton(ButtonName.Two_Seat);
            else if (index == 2) menuItem = getButton(ButtonName.Three_Seat);
            else menuItem = getButton(ButtonName.Four_Seat);
            menuItem.updateValueText(Settings.seatsSetting[index]);
        }
    }

    public static void setCustomerSpawnTimeVariation(int change) {
        if (Settings.customerSpawnTimeVariation + change <= 0) return;
        int val = Settings.customerSpawnTimeVariation += change;
        getButton(ButtonName.Spawn_Time_Variation).updateValueText(val);
    }

    public static void setCustomerSpawnTime(int change) {
        if (Settings.customerSpawnTime + change <= 0) return;
        int val = (int) (Settings.customerSpawnTime += change);
        getButton(ButtonName.Spawn_Time).updateValueText(val);
    }

    public static long getEatingTime() {

        return eatingTime;
    }

    public static void setEatingTime(long change) {
        if (Settings.eatingTime + change <= 0) return;
        int val = (int) (Settings.eatingTime += change);
        getButton(ButtonName.Eating_Time).updateValueText(val);
    }

    public static void setMaxNumWaiting(int change) {
        if (Settings.maxNumWaiting + change <= 0) return;
        int val = (Settings.maxNumWaiting += change);
        getButton(ButtonName.Max_Queue_Size).updateValueText(val);
    }

    private static ArrayList<Color> colors = new ArrayList<>(Arrays.asList(
            Color.rgb(63, 81, 113),
            Color.rgb(98, 85, 129),
            Color.rgb(141, 84, 132),
            Color.rgb(180, 84, 118),
            Color.rgb(205, 92, 92),
            Color.rgb(255, 154, 114),
            Color.rgb(255, 199, 76),
            Color.rgb(78, 201, 255),
            Color.rgb(62, 160, 204),
            Color.rgb(54, 140, 178)
    ));

    public static Color getRandomColor() {
        return colors.get(random.nextInt(colors.size()));
    }

    public static Random getRandom() {
        return random;
    }

    public static float getCustomerSize() {
        return customerSize;
    }

    public static long getMaxQueueSize() {
        return maxNumWaiting;
    }
}
