package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.gui.common.View;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AssignCoordinatorView implements View {
    @Override
    public Region getView() {
        var results = new VBox();
        results.getChildren().addAll(new Button("HEJ"), new Button("MED"), new Button("DIG"));
        return results;
    }
}
