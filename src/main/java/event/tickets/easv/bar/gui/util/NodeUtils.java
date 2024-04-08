package event.tickets.easv.bar.gui.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class NodeUtils {
    public static void bindVisibility(Node node, ObservableValue<Boolean> condition) {
        node.visibleProperty().bind(condition);
        node.managedProperty().bind(condition);
    }

    public static void bindVisibility(MenuItem menuItem, ObservableValue<Boolean> condition) {
        menuItem.visibleProperty().bind(condition);
    }
}
