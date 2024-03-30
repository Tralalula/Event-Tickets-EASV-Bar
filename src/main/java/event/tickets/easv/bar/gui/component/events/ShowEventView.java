package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ShowEventView implements View {
    private final EventModel model = EventModel.Empty();

    public ShowEventView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                model.update((EventModel) newData);
            }
        });
    }

    @Override
    public Region getView() {
        var results = new VBox();
        var title = new Label();
        title.textProperty().bind(model.title());
        title.getStyleClass().addAll(Styles.TITLE_1);

        results.getChildren().addAll(title);

        return results;
    }
}
