package sample;

import javafx.scene.layout.Pane;

public class Settings extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static float scale = 1;

    public static float tableSize = 50;
    public static float tableSpacing = 100;

    public static int queueSpacing = 150;

    public static long newCustomersTime = 1000;
    public static long annoyedTime = 1000;
    public static long eatingTime = 3000;

    public static void setScale(float scale) {
        Settings.scale = scale;
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
}
