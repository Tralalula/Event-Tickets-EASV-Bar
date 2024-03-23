package event.tickets.easv.bar.gui.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public class NodeUtils {
    public static void bindVisibility(Node node, ObservableValue<Boolean> condition) {
        node.visibleProperty().bind(condition);
        node.managedProperty().bind(condition);
    }
}
