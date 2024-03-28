package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ShowTicketView implements View {
    private MainModel model;

    public ShowTicketView(MainModel model) {
        this.model = model;
    }

    @Override
    public Region getView() {
        Label titleLabel = new Label();

        model.ticketModelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                titleLabel.textProperty().bind(newValue.title());
        });

        return new VBox(titleLabel);
    }
}
