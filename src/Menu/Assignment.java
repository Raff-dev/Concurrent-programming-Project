package Menu;

import Mechanism.CustomerGroup;
import Mechanism.Table;

public interface Assignment {
    boolean canAssign(CustomerGroup group, Table table);
}
