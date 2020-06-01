package Menu;

import java.util.HashMap;
import java.util.Map;

import static Menu.Launcher.menu;
import static Menu.Menu.Mode.RUNNING;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;
import static Menu.Launcher.scene;

public class InputHandler {
    private Map<Object, Task> pressBindings = new HashMap<>();
    private Map<Object, Task> releaseBindings = new HashMap<>();
    private Map<Object, Task> menuBindings = new HashMap<>();

    public InputHandler() {
        assignBindings();
        listen();
    }

    private void assignBindings() {
//        pressBindings.put(ESCAPE, menu::toggleMenu);
//        pressBindings.put(SPACE, () -> gameHandler.fall());
//        pressBindings.put(ENTER, () -> gameHandler.fall());
//        pressBindings.put(UP, () -> gameHandler.rotate());
//        pressBindings.put(DOWN, () -> gameHandler.move(0));
//        pressBindings.put(LEFT, () -> gameHandler.move(-1));
//        pressBindings.put(RIGHT, () -> gameHandler.move(1));
//
//        releaseBindings.put(DOWN,()->gameHandler.unMove(0));
//        releaseBindings.put(LEFT,()->gameHandler.unMove(-1));
//        releaseBindings.put(RIGHT,()->gameHandler.unMove(1));

        menuBindings.put(SHIFT, menu::toggleMenu);
        menuBindings.put(ESCAPE, menu::toggleMenu);
        menuBindings.put(SPACE, menu::toggleMenu);
        menuBindings.put(ENTER, menu::select);
        menuBindings.put(UP, () -> menu.switchSelection(-1));
        menuBindings.put(DOWN, () -> menu.switchSelection(1));
        menuBindings.put(LEFT, menu::leftArrow);
        menuBindings.put(RIGHT, menu::rightArrow);
    }

    private void listen() {
        scene.addEventFilter(KEY_PRESSED, event -> {
            if (menu.getMode() == RUNNING && pressBindings.containsKey(event.getCode()))
                pressBindings.get(event.getCode()).execute();
            else if (menuBindings.containsKey(event.getCode()))
                menuBindings.get(event.getCode()).execute();
        });
        scene.addEventFilter(KEY_RELEASED, event -> {
            if (releaseBindings.containsKey(event.getCode()))
                releaseBindings.get(event.getCode()).execute();
        });
    }
}
