package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.gui.common.UserModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class AssignCoordinatorModel {
    private final StringProperty search = new SimpleStringProperty("");
    private final BooleanProperty okToAssign = new SimpleBooleanProperty(true);
    private final Map<Integer, BooleanProperty> selectionStates = new HashMap<>();

    public StringProperty searchProperty() {
        return search;
    }

    public BooleanProperty okToAssignProperty() {
        return okToAssign;
    }

    public BooleanProperty selectionStateProperty(UserModel userModel) {
        return selectionStates.computeIfAbsent(userModel.id().get(), k -> new SimpleBooleanProperty(false));
    }
}
