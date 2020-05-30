package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Settings extends Pane {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static float scale = 1;
    public static float timeScale = 1;
    public static float floatieSize =25;

    public static int[] seatsSetting = new int[]{3, 2, 2, 2};
    public static float customerSize = 20;
    public static float tableSize = 50;
    public static float tableSpacing = 100;
    public static int queueSpacing = 100;

    public static int newCustomersTimeVariation = (int) (100 * timeScale);
    public static long newCustomersTime = (long) (100 * timeScale);
    public static long eatingTime = (long) (3000 * timeScale);
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

    public static float getTableSpacing() {
        return tableSpacing * scale;
    }

    public static float getQueueSpacing() {
        return queueSpacing * scale;
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
}
