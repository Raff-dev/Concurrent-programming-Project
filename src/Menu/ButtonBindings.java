package Menu;

import static Menu.Menu.*;
import static Menu.Menu.ButtonName.*;

public class ButtonBindings {
    public static void bind() {
        buttons.add(new MenuItem(Start,()-> System.out.println("start")));
        buttons.add(new MenuItem(Quit,()-> System.out.println("quit")));

    }
}
