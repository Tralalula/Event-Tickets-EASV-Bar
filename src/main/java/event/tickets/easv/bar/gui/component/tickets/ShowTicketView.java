package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ShowTicketView implements View {
    private TicketModel ticketModel;
    private final Label titleLabel = new Label();

    public ShowTicketView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                ticketModel = (TicketModel) newData;
                initializeBindings();
            }
        });
    }

    private void initializeBindings() {
        if (ticketModel != null) {
            titleLabel.textProperty().bind(ticketModel.title());
        }
    }

    @Override
    public Region getView() {
        return new VBox(titleLabel);
    }
}
