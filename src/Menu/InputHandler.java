package Menu;

import java.util.HashMap;
import java.util.Map;

import static Menu.Launcher.menu;
import static Menu.Launcher.scene;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class InputHandler {
    private Map<Object, Task> menuBindings = new HashMap<>();

    public InputHandler() {
        assignBindings();
        listen();
    }

    private void assignBindings() {
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
       if (menuBindings.containsKey(event.getCode()))
                menuBindings.get(event.getCode()).execute();
        });
    }
}
